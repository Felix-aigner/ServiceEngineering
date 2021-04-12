resource "exoscale_compute" "webservices" {
  display_name = "tf-managed-webservices"
  hostname = "webservices"

  security_group_ids = [exoscale_security_group.webservices.id]
  zone = var.zone

  key_pair = exoscale_ssh_keypair.stefan.id // <- hier erstmal nur Estepan für Tests
  template_id = data.exoscale_compute_template.ubuntu.id
  disk_size = 10
  // size = "micro" // für 2 java services in dieser Form wohl nicht ganz ausreichend
  size = "tiny"

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
# docker network create my-custom-bridged-net
# docker network rm my-custom-bridged-net # (info for removal, if need be)
#((docker run)) --network my-custom-bridged-net
# endregion

# region Run containers
# Webservice for Currency Conversion
docker run -d \
  -e CURRENCY_PORT=4000 \
  --network host \
  --name currency_conversion \
  shipitplz/currency-webservice

# Webservice for Car Rental
docker run -d \
  -e RENTAL_PORT=5000 \
  --network host \
  --name car_rental \
  shipitplz/car-rental-service

# Frontend
docker run -d \
  -e FRONTEND_PORT=80 \
  --network host \
  --name car_rental_frontend \
  shipitplz/car-rental-frontend
# endregion

EOF
}