// -------------------------------------------------------------------------- //
// Load Generation (Pool)
// -------------------------------------------------------------------------- //
resource "exoscale_security_group" "webservices" {
  name = "(Managed) Webservices group"
}

resource "exoscale_security_group_rule" "http" {
  security_group_id = exoscale_security_group.webservices.id
  type = "INGRESS"
  protocol = "tcp"
  cidr = "0.0.0.0/0"
  start_port = 80
  end_port = 80
}
resource "exoscale_security_group_rule" "https" {
  security_group_id = exoscale_security_group.webservices.id
  type = "INGRESS"
  protocol = "tcp"
  cidr = "0.0.0.0/0"
  start_port = 443
  end_port = 443
}

// SSH von aktueller (dynamischer) IP zulassen (ggf NICHT optimal für Produktioneinsetz, aber sehr stylish!!)
resource "exoscale_security_group_rule" "ssh" {
  security_group_id = exoscale_security_group.webservices.id
  type = "INGRESS"
  protocol = "tcp"
  cidr = "${chomp(data.http.myip.body)}/32" // Idee Leonardi Wagner: https://stackoverflow.com/a/53782560/1680919
  //cidr = "0.0.0.0/0"
  start_port = 22
  end_port = 22
}

//resource "exoscale_security_group_rule" "monitoring_metrics" {
//  security_group_id = exoscale_security_group.webservices.id
//  type = "INGRESS"
//  protocol = "tcp"
//  cidr = "0.0.0.0/0"
//  start_port = 9100
//  end_port = 9100
//}

resource "exoscale_security_group_rule" "car_rental" {
  security_group_id = exoscale_security_group.webservices.id
  type = "INGRESS"
  protocol = "tcp"
  cidr = "0.0.0.0/0"
  start_port = 5000
  end_port = 5000
}

resource "exoscale_security_group_rule" "currency_conversion" {
  security_group_id = exoscale_security_group.webservices.id
  type = "INGRESS"
  protocol = "tcp"
  cidr = "0.0.0.0/0"
  start_port = 4000
  end_port = 4000
}

//// -------------------------------------------------------------------------- //
//// Montioring (not yet)
//// -------------------------------------------------------------------------- //
//resource "exoscale_security_group" "monitoring" {
//  name = "(Managed) Monitoring group"
//}
//
//resource "exoscale_security_group_rule" "monitoring_access" {
//  security_group_id = exoscale_security_group.monitoring.id
//  type = "INGRESS"
//  protocol = "tcp"
//  cidr = "0.0.0.0/0"
//  start_port = 9090
//  end_port = 9090
//}
//
//// SSH von aktueller (dynamischer) IP zulassen (NICHT in Produktion einsetzen!)
//resource "exoscale_security_group_rule" "ssh" {
//  security_group_id = exoscale_security_group.monitoring.id
//  type = "INGRESS"
//  protocol = "tcp"
//  //cidr = "${chomp(data.http.myip.body)}/32" // Idee Leonardi Wagner: https://stackoverflow.com/a/53782560/1680919
//  cidr = "0.0.0.0/0"
//  start_port = 22
//  end_port = 22
//}
//
//resource "exoscale_security_group_rule" "grafana" {
//  security_group_id = exoscale_security_group.monitoring.id
//  type = "INGRESS"
//  protocol = "tcp"
//  cidr = "0.0.0.0/0"
//  start_port = 3000
//  end_port = 3000
//}
//
//resource "exoscale_security_group_rule" "monitoring_tests_via_http" {
//  security_group_id = exoscale_security_group.monitoring.id
//  type = "INGRESS"
//  protocol = "tcp"
//  cidr = "0.0.0.0/0"
//  start_port = 80
//  end_port = 80
//}
