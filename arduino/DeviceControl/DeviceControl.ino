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
#include <FastLED.h>          // Includes the FastLED library

// I2C Section
#define DEV_ADDRESS 0X4

// Device control commands
#define AllLEDsOff  0
#define RingLEDsRed 1
#define RingLEDsGreen 2
#define RingLEDsYellow 3
#define RingLEDsBlue 4
#define RingLEDsWhite 5
#define LEDStrip20vHigh 6
#define LEDStrip20vMed 7
#define LEDStrip20vLow 8
#define LEDStripGreen 9
#define LEDStripOrange 10
#define LEDStripRed 11
#define LEDStripBlue 12
#define LEDStripWhite 13
#define AllLEDsPattern 14


#define TRIG_PIN          12    // SR04 Ultrsound Sensor
#define ECHO_PIN          11    // SR04 Ultrsound Sensor
#define RING_LIGHT_PIN     6    // Ring Light control
#define STRIP_LIGHT_PIN    5    // Strip Light control
#define STRIP_LIGHT_20V    9    // Strip Ligh 20V Lighting control

// Ring Light Section
SR04 sr04 = SR04(ECHO_PIN, TRIG_PIN);

#define COLOR_ORDER GRB
#define CHIPSET     WS2812B   // WS2812B has 4 pins/LED, WS2812 has 6 pins/LED
#define NUM_LEDS    82

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
const CHSV WHITE(0, 0, 255);
const CHSV OFF(0, 0, 0);


// Strip Light Intensity
#define INTENSITY_HIGH 127
#define INTENSITY_MED  65
#define INTENSITY_LOW  0

// Variable hold object distance as seen by the ultrasonic sensor
double distance;

void setup() {
   
   // set up console baud rate.
   Serial.begin(9600);
   
  // Set up LED Control PIN
  pinMode (RING_LIGHT_PIN, OUTPUT);
  pinMode (STRIP_LIGHT_PIN, OUTPUT);
  pinMode (STRIP_LIGHT_20V, OUTPUT);

  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, RING_LIGHT_PIN, COLOR_ORDER>(leds, NUM_LEDS); // Initializes leds
  setAllLEDsColor(OFF);
  updateLEDs();

  // Turn off Onboard LED
  pinMode (13, OUTPUT);
  digitalWrite (13, LOW);
   
  // setup Ulrasonitsensor pins
  pinMode(TRIG_PIN, OUTPUT);     // Sets the trigPin as an Output
  pinMode(ECHO_PIN, INPUT);      // Sets the echoPin as an Input

  Wire.begin(DEV_ADDRESS);       // join i2c bus with address #4
  Wire.onReceive(receiveEvent);  // register callback to recieve events
  Wire.onRequest(requestEvent);  // register callback to request events
}

void loop() {
 
  distance = readUltrasonicSensor(); // Read the ultrsound sensor to see any objects nearby and store in global variable.

/*
 * LED Test section.
 
  // Test Ring Light Leds
  ledCommands(RingLEDsGreen);

  // Test Strip Lights 20V
  analogWrite(STRIP_LIGHT_20V, INTENSITY_HIGH);
  delay(1000);
  analogWrite(STRIP_LIGHT_20V, INTENSITY_MED);
  delay(1000);
  analogWrite(STRIP_LIGHT_20V, INTENSITY_LOW);

*/
  delay(1000);
}

// Listens to Wire for a request event and then reads the sensor distance value and sends it back on the I2C Wire.
void requestEvent() {
  String data;

  Serial.println("requestEvent - Enter");

  data = String(distance, 2);

  Serial.print("Ultrasonic sensor returned: ");
  Serial.println(data);

  // Write to the wire. Hope this works.
  Wire.write(data.c_str());
  Serial.println("requestEvent - Exit");
}

// Function to read ultrasonic sensor value to measure distance in cm.
double readUltrasonicSensor() {
  double dist = sr04.Distance(); // Distance read is in cm.
  // Serial.print(dist);
  // Serial.println(" - Cms");
  return dist;
}


// Wire callback will be called when event is recieved on the I2C BUS
void receiveEvent(int howMany)
{
  String LED = "";

  Serial.print("Received - ");
  Serial.println(howMany);

  while ( Wire.available() > 0 )
  {
    char n = (char)Wire.read();
    if (((int)n) > ((int)(' ')))
      LED += n;
  }
  Serial.print("Value = ");
  Serial.println(LED.c_str());
  ledCommands(LED.toInt());
}

// LED Control Section

void ledCommands(int cmd)
{
  Serial.print("Led Command -");
  Serial.println(cmd);
  switch (cmd) {
    case AllLEDsOff:
      setAllLEDsColor(OFF);
      break;

    case RingLEDsRed:
      setAllLEDsColor(RED);
      break;

    case RingLEDsGreen:
      setAllLEDsColor(GREEN);
      break;

    case RingLEDsYellow:
      setAllLEDsColor(YELLOW);
      break;

    case RingLEDsBlue:
      setAllLEDsColor(BLUE);
      break;
      
    case RingLEDsWhite:
      setAllLEDsColor(WHITE);
      break;
      
    case LEDStripGreen:
    case LEDStripOrange:
    case LEDStripRed:
    case LEDStripBlue:
    case LEDStripWhite:
      break;

    case LEDStrip20vHigh:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_HIGH);
      break;

    case LEDStrip20vMed:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_MED);
      break;

    case LEDStrip20vLow:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_LOW);
      break;

    case AllLEDsPattern:
    default:
      // Do nothing for now
      break;

  }
  delay(300);// 300ms delay for command to complete.
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

