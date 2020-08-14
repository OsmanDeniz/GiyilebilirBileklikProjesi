#define SENSOR_ADET 14
void SensorVerileriniYazdir(int veriDizisi[SENSOR_ADET][SENSOR_ADET]);

void setup() {
Serial.begin(9600);
pinMode(A0,INPUT);
pinMode(A1,INPUT);
pinMode(A2,INPUT);
pinMode(A3,INPUT);
pinMode(A4,INPUT);
pinMode(A5,INPUT);
pinMode(A6,INPUT);
pinMode(A7,INPUT);
pinMode(A8,INPUT);
pinMode(A9,INPUT);
pinMode(A10,INPUT);
pinMode(A11,INPUT);
pinMode(A12,INPUT);
pinMode(A13,INPUT);

pinMode(23, OUTPUT);
pinMode(24, OUTPUT);
pinMode(25, OUTPUT);
pinMode(26, OUTPUT);
pinMode(27, OUTPUT);
pinMode(28, OUTPUT);
pinMode(29, OUTPUT);
pinMode(30, OUTPUT);
pinMode(31, OUTPUT);
pinMode(32, OUTPUT);
pinMode(33, OUTPUT);
pinMode(34, OUTPUT); 
pinMode(35, OUTPUT);
pinMode(36, OUTPUT); 
}

void loop() {
 int sensorVerileri[SENSOR_ADET][SENSOR_ADET];
  /*  
  * Bu kısımda sensör üzerinden gelen veriler diziye atılmaktadır.
  * 
  */
  for(int satir=0;satir<SENSOR_ADET;satir++){
    for(int sutun=0;sutun<SENSOR_ADET;sutun++){
        digitalWrite(sutun+23,HIGH);//Bu kısımda IR Ledler için 23.dijital pinden itibaren hepsi 5v olarak ayarlanmaktadır.
        sensorVerileri[satir][sutun]=analogRead(satir); //Burada ise sırasıyla gönderilen sinyalin bir okuyucu tarafından okunup diziye atama işlemi yapılmaktadır.
        delay(10);
        digitalWrite(sutun+23,LOW); //5 volta çekilen pin tekrar 0 volta çekilmektedir.
        delay(10);
      }
  }
  SensorVerileriniYazdir(sensorVerileri);
}

void SensorVerileriniYazdir(int veriDizisi[SENSOR_ADET][SENSOR_ADET]){
  
  for(int satir=0;satir<SENSOR_ADET;satir++){
    for(int sutun=0;sutun<SENSOR_ADET;sutun++){
      Serial.print(String(veriDizisi[satir][sutun])+" - ");  //Burada dizi içeriği seri porta yazdırılmaktadır. 
    }
    Serial.println();
  }
  Serial.println();  
  Serial.println();

}
