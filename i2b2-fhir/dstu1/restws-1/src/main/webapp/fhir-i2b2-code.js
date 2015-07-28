//var fhirServerBase="http://i2b2vmdev3.dipr.partners.org:8080";
	 
	
    var DScounter=0;
	function DataStore(url,msg) {
		this.id=1;
    	this.url = url;
    	this.msg = msg;
        this.getMsg = function () {
            return this.msg;
        };
        this.getId = function () {
            return this.id;
        };
        this.getUrl = function () {
            return this.url;
        };   
    };

   var DSArray=new Array();
   //var ds1=new DataStore("http://nowhere","msg1");
   
   var ds1=new DataStore(fhirServerBase+"/Patient","Get all patients");
   var ds2=new DataStore(fhirServerBase+"/Patient/1000000005","Get particular patient");
   var ds3=new DataStore(fhirServerBase+"/MedicationPrescription?patient=1000000005","Get Medication Prescriptions for a particular patient");
   var ds4=new DataStore(fhirServerBase+"/Observation?subject=1000000005","Get Labs for a particular patient");
   var ds5=new DataStore(fhirServerBase+"/MedicationPrescription?patient=1000000005&_include=MedicationPrescription.Medication&_include=MedicationPrescription.Patient","Get Medication Prescriptions for a particular patient and include Medications and the Patient");
   var ds6=new DataStore(fhirServerBase+"/Condition?subject=1000000005","Get Conditions for particular Patients");
   var ds7=new DataStore(fhirServerBase+"/Patient?gender=M&birthdate=<1970-01-01","Get Male Patients born before 1970");
   
   //var ds6=new DataStore(fhirBase+"Patient?gender=female&birthdate=<1970-01-01&@Patient.maritalStatus.coding.code:exact=M");
   
   DSArray.push(ds1);
   DSArray.push(ds2);
   DSArray.push(ds3);
   DSArray.push(ds4);
   DSArray.push(ds5);
   DSArray.push(ds6);
   DSArray.push(ds7);
   
   var theDiv=document.getElementById("nav1");
   //theDiv.innerHTML='<div style="width:100%;" onclick="return null">Use Cases</div>';
   
    for	(index = 0; index < DSArray.length; index++) {
    	var d=DSArray[index];
         //  code.value=code.value+d.getMsg();
           theDiv.innerHTML = theDiv.innerHTML +
          // '<a href="#Foo" onclick="return runMyFunction(\''+d.getUrl()+'\'); " >'+d.getMsg()+'</a><hr>';
          // '<button style="width:40px;">Some really long text that is more than 20px</button>';
        '<button type=button  onclick="return runMyFunction(\''+d.getUrl()+'\'); "  >'+d.getMsg()+'</button> ';
       //    '<button type=button  onclick="return runMyFunction(\''d.getId()+'\',this); " >'+d.getMsg()+'</button> ';
         //  theDiv.innerHTML = "hi"
	}
    
