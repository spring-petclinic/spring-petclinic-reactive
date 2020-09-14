echo "🚀 Go to https://astra.datastax.com/org/?create_service_account
If you have not created a service account for your org click Actions -> Add Service Account. Then, click the copy icon and paste your service account credentials here: "
  read -r SERVICE_ACCOUNT
  export SERVICE_ACCOUNT="${SERVICE_ACCOUNT}"

echo "Getting your Astra DevOps API token..."
DEVOPS_TOKEN=$(curl -s --request POST \
  --url "https://api.astra.datastax.com/v2/authenticateServiceAccount" \
  --header 'content-type: application/json' \
  --data "$SERVICE_ACCOUNT" | jq -r '.token')

echo "Getting databases..."
DBS=$(curl -s --request GET \
  --url "https://api.astra.datastax.com/v2/databases?include=nonterminated&provider=all&limit=25" \
  --header "authorization: Bearer ${DEVOPS_TOKEN}" \
  --header 'content-type: application/json')

# TODO: Allow the user to select the DB
NUM_DBS=$(echo "${DBS}" | jq -c 'length')
FIRST_DB_ID=$(echo "${DBS}" | jq -c '.[0].id')
FIRST_DB_REGION=$(echo "${DBS}" | jq -c '.[0].info.region')
FIRST_DB_USER=$(echo "${DBS}" | jq -c '.[0].info.user')

# TODO: Allow the user to select a keyspace
FIRST_DB_KEYSPACE=$(echo "${DBS}" | jq -c '.[0].info.keyspaces[0]')
FIRST_DB_SECURE_BUNDLE_URL=$(echo "${DBS}" | jq -c '.[0].info.datacenters[0].secureBundleUrl')

export ASTRA_SECURE_BUNDLE_URL=${FIRST_DB_SECURE_BUNDLE_URL}
gp env ASTRA_SECURE_BUNDLE_URL=${FIRST_DB_SECURE_BUNDLE_URL} &>/dev/null

# Download the secure connect bundle
curl -s -L $(echo $FIRST_DB_SECURE_BUNDLE_URL | sed "s/\"//g") -o astra-creds.zip

export ASTRA_DB_BUNDLE="astra-creds.zip"
gp env ASTRA_DB_BUNDLE="astra-creds.zip" &>/dev/null

export ASTRA_DB_USERNAME=$(echo ${FIRST_DB_USER} | sed "s/\"//g")
gp env ASTRA_DB_USERNAME=$(echo ${FIRST_DB_USER} | sed "s/\"//g") &>/dev/null

export ASTRA_DB_KEYSPACE=$(echo ${FIRST_DB_KEYSPACE} | sed "s/\"//g")
gp env ASTRA_DB_KEYSPACE=$(echo ${FIRST_DB_KEYSPACE} | sed "s/\"//g") &>/dev/null

export ASTRA_DB_ID=$(echo ${FIRST_DB_ID} | sed "s/\"//g")
gp env ASTRA_DB_ID=$(echo ${FIRST_DB_ID} | sed "s/\"//g") &>/dev/null

export ASTRA_DB_REGION=$(echo ${FIRST_DB_REGION} | sed "s/\"//g")
gp env ASTRA_DB_REGION=$(echo ${FIRST_DB_REGION} | sed "s/\"//g") &>/dev/null

if [[ -z "$ASTRA_DB_PASSWORD" ]]; then
  echo "What is your Astra DB password? 🔒"
  read -s ASTRA_DB_PASSWORD
  export ASTRA_DB_PASSWORD=${ASTRA_DB_PASSWORD}
  gp env ASTRA_DB_PASSWORD=${ASTRA_DB_PASSWORD} &>/dev/null
fi

echo "You're all set 👌"
