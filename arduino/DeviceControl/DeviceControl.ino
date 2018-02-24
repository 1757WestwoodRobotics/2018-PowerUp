/**
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.

   Since there are two ringlights and one array of leds,
   configure the loops properly. 0-23 or 24-48.

   Controls Ultrasound Sensor SR04 from Elegoo 
*/
#include <Wire.h>
#include <SR04.h>

// I2C Section
#define DEV_ADDRESS 0X4

// SR04 Ultrsound Sensor section
#define TRIG_PIN 12
#define ECHO_PIN 11

SR04 sr04 = SR04(ECHO_PIN, TRIG_PIN);

// Ring Light Section
#define DATA_PIN    6
#define COLOR_ORDER GRB
#define CHIPSET     WS2812B   // WS2812B has 4 pins/LED, WS2812 has 6 pins/LED
#define NUM_LEDS    48
#define DEV_ADDRESS 4

CRGB leds[NUM_LEDS];

// Default led HSV configurations
int led1_hue = 100;
int led1_sat = 255;
int led1_val = 255;
int led2_hue = 255;
int led2_sat = 255;
int led2_val = 255;

// Preset values
const CHSV GREEN(100, 255, 255);
const CHSV RED(0, 255, 255);
const CHSV BLUE(160, 255, 255);
const CHSV YELLOW(64, 255, 255);
const CHSV OFF(0, 0, 0);

void setup() {

  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, DATA_PIN, COLOR_ORDER>(leds, NUM_LEDS); // Initializes leds
  setAllLEDsColor(OFF);
  updateLEDs();

  Serial.begin(9600);
  pinMode (13, OUTPUT);
  digitalWrite (13, LOW);
  Wire.begin(DEV_ADDRESS);                // join i2c bus with address #4
  Wire.onReceive(receiveEvent); // register callback to recieve events
  Wire.onRequest(requestEvent); // register callback to request events
}

void loop() {
  double a; 
  a = readSensor(); // Read the ultrsound sensor to see any objects nearby
  delay(1000);
}

// Listens to Wire for a request event and then reads the sensor value and sends it back on the I2C Wire.
void requestEvent(){
  double dist;
  String data;
  
  dist = readSensor();
  data = String(dist, 2);

  // Write to the wire
   Wire.beginTransmission(DEV_ADDRESS);
   Wire.write(data.c_str());
   Wire.endTransmission();     // stop transmitting
}

// Function to read sensor value and convert it to distance in inches.
double readSensor() {
  double distance = sr04.Distance()/ 2.54; // Distance read is in cm. convert it to Inches
  Serial.print(distance);
  Serial.println(" - Inches");
  return distance;
}


// Wire callback will be called when event is recieved on the I2C BUS
void receiveEvent(int howMany)
{
  String LED = "";

  Serial.println(howMany);

  while ( Wire.available() > 0 )
  {
    char n = (char)Wire.read();
    if (((int)n) > ((int)(' ')))
      LED += n;
  }

  Serial.println(LED.c_str());
  ledCommands(LED);
}

// LED Control Section

void ledCommands(String cmd)
{
  if (cmd == "1")
  {
    setAllLEDsColor(RED);
    digitalWrite (13, HIGH);
  }
  else if (cmd == "2")
  {
    setAllLEDsColor(GREEN); 
    digitalWrite (13, HIGH);
  }

  else if (cmd == "3")
  {
    setAllLEDsColor(YELLOW);
    digitalWrite (13, HIGH);
  }

  else
  {
    setAllLEDsColor(OFF);
    digitalWrite (13, LOW);
  }
  delay(1000);
}


// All LEDs display functions

void updateLEDs() {
  FastLED.show();
  FastLED.delay(30);
}

void setAllLEDsColor(CHSV color) {
  fill_solid(leds, NUM_LEDS, color);
  updateLEDs();
}

void setAllLEDsRainbow(uint8_t initialhue, uint8_t deltahue) {
  fill_rainbow(leds, NUM_LEDS, initialhue, deltahue);
  updateLEDs();
}

void sequentialRainbow(uint8_t initialhue, uint8_t deltahue, int msDelay) {
  uint8_t hue = initialhue + deltahue;
  for (int i = 0; i < NUM_LEDS; i++) {
    leds[i] = CHSV(hue, 255, 255);
    updateLEDs();
    delay(msDelay);
    hue += deltahue;
  }
}

void chaseTheLED(int startingLED, int msDelay, int howManyTimes, CHSV color) {    // howManyTimes isn't implemented yet.
  for (int i = startingLED; i < NUM_LEDS; i++) {
    setIndividualLED(i, color);
    FastLED.delay(msDelay);
    leds[i] = OFF;
  }
}

// Individual LED functions
void setIndividualLED(int led, CHSV color) {
  leds[led] = color;
  updateLEDs();
}

// Individual ringlight functions
void setRingLightHue(int whichLight, int hue) {
  switch (whichLight) {
    case 1:
      for (int i = 0; i < 24; i++) {
        leds[i] = CHSV(hue, led1_sat, led1_val);
      }
      led1_hue = hue;
      break;
    case 2:
      for (int i = 24; i < 48; i++) {
        leds[i] = CHSV(hue, led2_sat, led2_val);
      }
      led2_hue = hue;
      break;
  }
  updateLEDs();
}

void setRingLightSaturation(int whichLight, int saturation) {
  switch (whichLight) {
    case 1:
      for (int i = 0; i < 24; i++) {
        leds[i] = CHSV(led1_hue, saturation, led1_val);
      }
      led1_sat = saturation;
      break;
    case 2:
      for (int i = 24; i < 48; i++) {
        leds[i] = CHSV(led2_hue, saturation, led2_val);
      }
      led2_sat = saturation;
      break;
  }
  updateLEDs();
}

void setRingLightValue(int whichLight, int value) {
  switch (whichLight) {
    case 1:
      for (int i = 0; i < 24; i++) {
        leds[i] = CHSV(led1_hue, led1_sat, value);
      }
      led1_val = value;
      break;
    case 2:
      for (int i = 24; i < 48; i++) {
        leds[i] = CHSV(led2_hue, led2_sat, value);
      }
      led2_val = value;
      break;
  }
  updateLEDs();
}

