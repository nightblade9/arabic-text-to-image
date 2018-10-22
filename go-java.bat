rd /s /q output
mkdir output

javac -encoding UTF-8 -cp "gson-2.8.2.jar;"  TextToGraphics.java Word.java
java -cp ".;gson-2.8.2.jar" TextToGraphics
