module "environment" {
  source = "./modules/environment"
  depends_on = [module.management]
  providers = {
    google      = google.dev
    google-beta = google-beta.dev
  }
  project_id              = google_project.dev_project.project_id
  api_key                 = var.api_key
  domain                  = "dev.${var.domain}"
  prefix                  = var.prefix
  fact_service_name       = var.fact_service_name
  profile_service_name    = var.profile_service_name
  skill_service_name      = var.skill_service_name
  region                  = var.region
  management_project_id   = var.management_project_id
  skill_service_version   = var.skill_service_dev_version
  fact_service_version    = var.fact_service_dev_version
  profile_service_version = var.profile_service_dev_version
  container_repo          = var.container_repo
  project_name            = var.dev_project_name
  profile_database_name   = var.profile_database_name
  fact_database_name      = var.fact_database_name
  fact_database_user      = var.fact_database_user
}

provider "google" {
  alias   = "dev"
  project = var.dev_project_id
  region  = var.region
}

provider "google-beta" {
  alias   = "dev"
  project = var.dev_project_id
  region  = var.region
}

resource "google_project" "dev_project" {
  project_id      = var.dev_project_id
  name            = var.dev_project_name
  billing_account = var.billing_account
  labels = {
    "firebase" = "enabled"
  }
}