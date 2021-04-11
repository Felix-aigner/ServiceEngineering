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
  name = "Zega"
  public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEAgwJB/1FI6TFjTM5a4m9r8y1sLlDNpFc8eIuKL1/pZf/wUFKCL4U+dR7HdJ3s4qVPJugxSDTzTKGGQHjSqkqshQ+JqfdY/mJS2aG7UZEBUndHxWrUokj19tXW52CnZa1x7c3Q84nQOluDOlQWSHIeCk+z+FmJW4ldr1++dfjXxl5PiobzdbwSds2Ttf9NtxHkOpMK4rT3gozkYvrmJJZcuXJlkLIeRurhuEbj7HhOn9gMuf1apnYB2jHw4T3yWOAqltnsAJmmwYDLdz6PCbIMk3zSiWI2gjP/6kVaT7yZAQp7CZu5hsguF50AahUll6dcUVjHAXjh4M9J2W98YRUtKQ=="
}

data "exoscale_compute_template" "ubuntu" {
  zone = var.zone
  name = var.default_distro // Ubuntu 20.04 LTS
}