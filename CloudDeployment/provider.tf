terraform {
  required_providers {
    exoscale = {
      source  = "exoscale/exoscale"
      version = "~> 0.20.0"
    }
  }
}

provider "exoscale" {
  key = var.exoscale_key
  secret = var.exoscale_secret
}