echo "Building modules in correct order..."

mvn clean install -f user-administration/pom.xml
mvn clean install -f score-administration/pom.xml
mvn clean install -f vocabulary-administration/pom.xml
mvn clean install -f game-administration/pom.xml
mvn clean install -f round-administration/pom.xml

echo "Clean install finished!"
