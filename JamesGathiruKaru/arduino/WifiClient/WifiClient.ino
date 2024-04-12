/*
    This sketch establishes a TCP connection to a "quote of the day" service.
    It sends a "hello" message, and then prints received data.
*/

#include <ESP8266WiFi.h>
#include <ArduinoJson.h>

#ifndef STASSID
#define STASSID "OPPO Reno"
#define STAPSK "jkaru8767"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;

const char* host = "192.168.71.23";
const uint16_t port = 8085;
String reading = "";

String httpRequestData = "";//["{\"name\":\"component7\",\"usageValue\":9}"]

void setup() {
  pinMode(12, OUTPUT);
  Serial.begin(9600);
  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}



void loop() {
   static bool wait2 = true;
  static bool wait = false;
  if(Serial.available() > 0){
    wait2 = false;
    reading = Serial.readString();
    reading.trim();
  httpRequestData = "{\"username\":\"james@gmail.com\",\"components\":[";
  httpRequestData += reading;
  httpRequestData += "]}";
  Serial.println(httpRequestData);
  Serial.print("connecting to ");
  Serial.print(host);
  Serial.print(':');
  Serial.println(port);

WiFiClient client;
     // Use WiFiClient class to create TCP connections
  if (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(5000);
    return;
  }

  // This will send a string to the server
  Serial.println("sending data to server");
  if (client.connected()) { 
   client.print("POST /units HTTP/1.1\r\n");
    client.print("Host: ");
    client.print(host);
    client.print("\r\n");
    client.print("Content-Type: application/json\r\n");
    client.print("Content-Length: ");
    client.print(httpRequestData.length());
    client.print("\r\n\r\n");
    client.print(httpRequestData);
     }

  // wait for data to be available
  unsigned long timeout = millis();
  while (client.available() == 0) {
    if (millis() - timeout > 5000) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      delay(3000);
      return;
    }


  // Read all the lines of the reply from server and print them to Serial
  Serial.println("receiving from remote server");
  // not testing 'client.connected()' since we do not need to send data here
  while (client.available()) {
    char ch = static_cast<char>(client.read());
    Serial.print(ch);
  }

  // Close the connection
  Serial.println();
  Serial.println("closing connection");
  client.stop();
  
  if (wait) {
    delay(1000);  // execute once every 3 seconds, don't flood remote service
  }
  wait = true;
}
delay(1000);

  }

WiFiClient client;
     // Use WiFiClient class to create TCP connections
  if (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(5000);
    return;
  }

   // Make an HTTP GET request
  client.print("GET /ard/getComponents/james@gmail.com HTTP/1.0\r\n");
  client.print("Host: ");
  client.print(host);
  client.print("\r\n");
  client.print("Connection: close\r\n\r\n");

  Serial.println("Request sent");

  // Wait for the server to respond
  while (client.connected()) {
    if (client.available()) {
      // Skip HTTP headers
            while (client.connected())
        {
            String line = client.readStringUntil('\n');
            if (line == "\r")
            {
                Serial.println("headers received");
                break;
            }
        }

    // String line = client.readString();

    // JSON
    // Allocate the JSON document
    const size_t capacity = JSON_ARRAY_SIZE(10) + 10 * JSON_OBJECT_SIZE(2) + 10 * JSON_OBJECT_SIZE(3) + 10 * JSON_OBJECT_SIZE(5) + 10 * JSON_OBJECT_SIZE(8) + 3730;
    DynamicJsonDocument doc(capacity);

    // Parse JSON object
    DeserializationError error = deserializeJson(doc, client);
      

      if (error) {
        Serial.print("deserializeJson() failed: ");
        Serial.println(error.c_str());
        delay(2000);
        return;
      }

      // Handle the JSON array response
      JsonArray array = doc.as<JsonArray>();

      // Iterate over the array elements
      for (JsonVariant value : array) {
         JsonObject component = value.as<JsonObject>();
        int id = component["id"];
        const char* username = component["username"];
        const char* componentName = component["componentName"];
        bool status = component["status"];

        // // Print or process each object
        // Serial.print("ID: ");
        // Serial.println(id);
        // Serial.print("Username: ");
        // Serial.println(username);
        // Serial.print("Component Name: ");
        // Serial.println(componentName);
        // Serial.print("Status: ");
        // Serial.println(status ? "true" : "false");
        if(component["componentName"] =="power outlets"){
          Serial.println(componentName);
          if(component["status"]){
            digitalWrite(12, HIGH);
          }else{
            digitalWrite(12, LOW);
          }
        }
      }
    }
    }
 if(wait2){
   delay(2000);
 }
 
}
