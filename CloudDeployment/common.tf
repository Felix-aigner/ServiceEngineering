variable "exoscale_key" {
  description = "The Exoscale API key"
  type = string
}

variable "exoscale_secret" {
  description = "The Exoscale API secret"
  type = string
}

data "http" "myip" {
  url = "https://ipv4.icanhazip.com"
}

variable "zone" {
  default = "at-vie-1"
}

variable "default_distro" {
  default = "Linux Ubuntu 20.04 LTS 64-bit"
}

////////////////////////////////////////////////////////////////////////////////////////////////////

resource "exoscale_ssh_keypair" "stefan" {
  name = "StraStra"
  public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCKkIInMGHJY6D1SpkXaBjmFajiFVCO91VrwE2bpx6COe/NquqHN7xr8sg+VSp8O+odlaad4znCQoppZKmgb6Ko2Qzu4RZHl/uU524cRqCQNrXQxSVS45Ix3+5ziyXV3ze7uarSBU7DXzAaahpRPY/I93jHZ72AdGr3z4zfIsUX3w=="
}

resource "exoscale_ssh_keypair" "iwer_anders_von_den_jungs" {
  name = "joe"
  public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC11W77TOegP3vGt2fn9KwXNzksTIAEcBpbo+/2gvm5NOUUVgGKoiXVMC87Tv52p6oVIxOvLEDH1AhFkLVS6xPLv2xUkv/IwQW2cmTuxBmKBjk58cq8Zy1EGbtfpdoEYRzfWaWtEUHnyPFtnc8wDekTYZa2aLXmFzVcWYxot7hqEOVcDKTrzAPv9iKGy8yixWu8wfE9UoZVg1gkg7Vw/IrmA4JDFcoIIBvo/IGLbG271ZaHV5ZG/ChOyVSTSvC0IEN30e0HHHLKWr1ASxojC/orjeITnh/2UYQWPbfVIJm6xRp5W5JzI/h514vU+mAW5mir3bphPFQjKpSO67BVyNXL johan@Joe-Desktop"
}

data "exoscale_compute_template" "ubuntu" {
  zone = var.zone
  name = var.default_distro // Ubuntu 20.04 LTS
}
