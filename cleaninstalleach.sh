echo "Building modules in correct order...Please make sure you have called this Shell script from no different directory than the repository root or else it will lead to nothing but avoidable frustration due to failed operations."

mvn clean install -f user-administration/pom.xml
mvn clean install -f vocabulary-administration/pom.xml
mvn clean install -f game-administration/pom.xml

echo "Clean install finished. In case of doubt, please check the logs above yourself in order to find out whether the operations were successful or failed."
