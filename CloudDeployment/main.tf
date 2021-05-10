resource "exoscale_compute" "webservices" {
  display_name = "tf-managed-webservices"
  hostname = "webservices"

  security_group_ids = [exoscale_security_group.webservices.id]
  zone = var.zone

  key_pair = exoscale_ssh_keypair.stefan.id // <- hier erstmal nur Estepan für Tests
  template_id = data.exoscale_compute_template.ubuntu.id
  disk_size = 10
  // size = "micro" // für 2 java services in dieser Form wohl nicht ganz ausreichend
  size = "medium"

  // partially based on https://gist.github.com/janoszen/7ced227c54d1c9e86a9c1cbd93a451f2
  user_data = <<EOF
#!/bin/bash
set -e
export DEBIAN_FRONTEND=noninteractive
# mkdir -p /srv/webservices/carrental/

# region Install Docker
apt-get update
apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
apt-key fingerprint 0EBFCD88
add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
apt-get update
apt-get install -y docker-ce docker-ce-cli containerd.io
# endregion

# region User-defined bridge network (grafana / prometheus / scaler)
# https://docs.docker.com/network/bridge/#differences-between-user-defined-bridges-and-the-default-bridge
# (in order to more nicely reference from one dockerinstance to the other (config))
docker network create carrental-net
# docker network rm my-custom-bridged-net # (info for removal, if need be)
#((docker run)) --network my-custom-bridged-net
# endregion

# Own IP via exoscale service
#RENTAL_IP=$(curl http://metadata.exoscale.com/latest/meta-data/public-ipv4)
#echo $RENTAL_IP
echo "\n\n\n\n\n"
echo "################# Here starts the core cloud init #################"
# -----------------------------------------------------------------------------------------------------------------

mkdir -p /srv/rabbitmq/config/

# RabbitMQ (definitions.json)
echo \
'{
  "rabbit_version": "3.7.6",
  "users": [
    {
      "name": "guest",
      "password_hash": "MQdLugKr3mH+/F80LFlcmSCmW7PzRIs2mhkw+K84COXmhIot",
      "hashing_algorithm": "rabbit_password_hashing_sha256",
      "tags": "administrator"
    }
  ],
  "vhosts": [
    {
      "name": "/"
    }
  ],
  "permissions": [
    {
      "user": "guest",
      "vhost": "/",
      "configure": ".*",
      "write": ".*",
      "read": ".*"
    }
  ],
  "topic_permissions": [],
  "parameters": [],
  "global_parameters": [
    {
      "name": "cluster_name",
      "value": "rabbit@my-rabbit"
    }
  ],
  "policies": [],
  "queues": [
    {
      "name": "hello",
      "vhost": "/",
      "durable": true,
      "auto_delete": false,
      "arguments": {}
    },
    {
      "name": "hello2",
      "vhost": "/",
      "durable": true,
      "auto_delete": false,
      "arguments": {}
    },
    {
      "name": "getUser",
      "vhost": "/",
      "durable": true,
      "auto_delete": false,
      "arguments": {}
    }
  ],
  "exchanges": [],
  "bindings": []
}
' > /srv/rabbitmq/config/definitions.json

# RabbitMQ (rabbitmq.config)
echo \
'[
  {
    rabbit,
      [
        { loopback_users, [] }
      ]
  },
  {
    rabbitmq_management,
      [
        { load_definitions, "/etc/rabbitmq/definitions.json" }
      ]
  }
].' > /srv/rabbitmq/config/rabbitmq.config



# Frontend preparations
#mkdir -p /usr/share/nginx/html/assets
mkdir -p /srv/frontend/
echo \
"{
  \"restUrl\":     \"http://$(curl http://metadata.exoscale.com/latest/meta-data/public-ipv4):5000/\",
  \"currencyUrl\": \"http://$(curl http://metadata.exoscale.com/latest/meta-data/public-ipv4):4000/\"
}" > /srv/frontend/public-config.json


# ---------------------------------------------
# Database (RabbitMQ & MongoDB)
docker run -d \
  --network host \
  -p 5672:5672 \
  -v /srv/rabbitmq/config/:/etc/rabbitmq/ \
  --hostname my-rabbit --name some-rabbit rabbitmq:3

docker run -d \
  --network host \
  --name mongo \
  -e MONGO_INITDB_DATABASE=userdb \
  -e MONGO_INITDB_DATABASE=cardb \
  -e MONGO_INITDB_DATABASE=rentaldb \
  mongo
# -p 27017:27017


# ---------------------------------------------
# Frontend
# ---------------------------------------------
docker run -d \
  -e FRONTEND_PORT=80 \
  -e RENTAL_PORT=5000 \
  -e RENTAL_IP \
  --mount type=bind,source=/srv/frontend/public-config.json,target=/usr/share/nginx/html/assets/public-config.json \
  --network host \
  --name frontend \
  shipitplz/se-frontend

# ---------------------------------------------
# Webservice for REST (5000 => testing)
# ---------------------------------------------
docker run -d \
  -p 5000:5000 \
  -e RENTAL_PORT=5000 \
  -e PORT=5000 \
  --network host \
  --name rest \
  shipitplz/se-rest-service

# ---------------------------------------------
# ---------------------------------------------

# Car Service
docker run -d \
  -e RENTAL_PORT=5000 \
  --network host \
  --name car_service \
  shipitplz/se-car-service

# Rental Service
docker run -d \
  --network host \
  --name rental_service \
  shipitplz/se-rental-service

# User Service
docker run -d \
  --network host \
  --name user_service \
  shipitplz/se-user-service

# ---------------------------------------------
# ---------------------------------------------

# Webservice for Currency Conversion (4000 => testing)
docker run -d \
  -p 4000:4000 \
  -e CURRENCY_PORT=4000 \
  --network host \
  --name currency_conversion \
  shipitplz/currency-webservice

EOF
}
