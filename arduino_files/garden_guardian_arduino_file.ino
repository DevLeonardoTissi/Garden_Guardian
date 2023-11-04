

#define yellowLed 42
#define redLed 43
#define blueLed 44

#define rgbLedRedPort 48
#define rgbLedGreenPort 49
#define rgbLedBluePort 50

#define buzzerPin 34

const int humiditySensorPort = A0;
const int potentiometerPort = A1;


void setup() {
  
  pinMode(yellowLed, OUTPUT);
  pinMode(redLed, OUTPUT);
  pinMode(blueLed, OUTPUT);
  pinMode(humiditySensorPort, INPUT);
  
  pinMode(rgbLedRedPort, OUTPUT);
  pinMode(rgbLedGreenPort, OUTPUT);
  pinMode(rgbLedBluePort, OUTPUT);

  pinMode(buzzerPin, OUTPUT);
  
  Serial.begin(9600);

}

void loop() {

  int humiditySensorValue = analogRead(humiditySensorPort);
  int sensorValue = humiditySensorValue;
  int mappedValue = map(sensorValue, 0, 1023, 100, 0);
 

  if (mappedValue > 60) {
  digitalWrite(blueLed, HIGH);
  digitalWrite(yellowLed, LOW);
  digitalWrite(redLed, LOW);

  digitalWrite(rgbLedBluePort, HIGH);
  digitalWrite(rgbLedGreenPort, LOW);
  digitalWrite(rgbLedRedPort, LOW);

  noTone(buzzerPin);
  

} else if (mappedValue >= 40 && mappedValue <= 60) {
  digitalWrite(blueLed, LOW);
  digitalWrite(yellowLed, HIGH);
  digitalWrite(redLed, LOW);

  digitalWrite(rgbLedBluePort, LOW);
  digitalWrite(rgbLedGreenPort, HIGH);
  digitalWrite(rgbLedRedPort, HIGH);

  noTone(buzzerPin);

} else {
  digitalWrite(blueLed, LOW);
  digitalWrite(yellowLed, LOW);
  digitalWrite(redLed, HIGH);

  digitalWrite(rgbLedBluePort, LOW);
  digitalWrite(rgbLedGreenPort, LOW);
  digitalWrite(rgbLedRedPort, HIGH);

  tone(buzzerPin, 261,250);

}
  
  
  String mappedString = String(mappedValue);
  Serial.println(mappedString);

  delay(analogRead(potentiometerPort));

}
