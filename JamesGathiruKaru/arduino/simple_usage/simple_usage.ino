/**
 * This program shows you how to use the basics of this library.
*/

#include "EmonLib.h" 

EnergyMonitor emon1;

#define SENSITIVITY 500.0f
String reading = "";

// ZMPT101B sensor output connected to analog pin A0
// and the voltage source frequency is 50 Hz.


void setup() {
  Serial.begin(9600);

  // Change the sensitivity value based on value you got from the calibrate
  // example.
  emon1.voltage(A0, 227.0, 1.7);  // Voltage: input pin, calibration, phase_shift
  emon1.current(A1, 4.75);       // Current: input pin, calibration.

}

void loop() {
  emon1.calcVI(20,2000);  
reading = "{\"name\":\"power outlet\",\"usageValue\":";
  // read the voltage and then print via Serial.

reading += emon1.realPower;
reading += "}";//if there is only one component
// reading += "},";



// reading += "{\"name\":\"component8\",\"usageValue\":";
// reading += watts;
// //reading += "}"; 
// reading += "},";//add more

// reading += "{\"name\":\"component9\",\"usageValue\":";
// reading += watts;
// reading += "}"; 

  char buffer[reading.length() + 1];
  reading.toCharArray(buffer, reading.length() + 1);
  Serial.write(buffer);
  delay(3500);
}




float getWatts(float current, float voltage){
  return current * voltage;
}