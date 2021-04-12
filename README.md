# ServiceEngineering
Semester Project for SS21

## Cloud Deployment

Cloud Deployment is accomplished via exoscale and terraform

### Prerequisites
 - An existing exoscale account: https://portal.exoscale.com/register
 - An instance of `terraform` preinstalled, and configured in `PATH`, ready to be called from commandline via "`terraform ...`"

### Create an API key
 - Login to exoscale portal: https://portal.exoscale.com/login
 - Click on `IAM` (Identity and Access Management, main menu to the left)
 - Click on `Add` (green button to the upper right) and specify an arbitrary `key name`

### Specify key and secret
 - Within the projects `CloudDeployment` folder create (or copy) a `terraform.tfvars` file
 - OPTIONAL: Its contents may be copied from the existing `terraform.tfvars.template` and edited
 - Specify `exoscale_key = "[KEY]"`, so that `[KEY]` corresponds to the newly created key name
 - Specify `exoscale_secret = "[SECRET]"`, so that `[SECRET]` corresponds to the newly created (only briefly shown) key secret

### Run the automated deployment script
 - For Windows (XP and higher), it is sufficient to double-click the `apply.cmd` within the `CloudDeployment` subfolder
 - For other plattforms (macOS, Linux, etc.) it is necessary to use/navigate to the `CloudDeployment` subfolder and run 
   - `terraform init`
   - `terraform apply -auto-approve`
 
 