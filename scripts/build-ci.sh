cd ./angular-bootcamp-repo

# Build API
./gradlew build -x test

# Copy API to output folder
cp ./app_api/build/libs/angular-bootcamp-0.0.1-SNAPSHOT.jar ../output

# Build UI
cd ./app_ui
npm install && ng build --environment=prod

# Copy UI to output folder
cp -r dist ../../output