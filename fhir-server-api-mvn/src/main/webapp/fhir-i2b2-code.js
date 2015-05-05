 			
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
   
   var ds1=new DataStore("http://localhost:8080/fhir-server-api-mvn/a/a/Patient","Get all patients");
   var ds2=new DataStore("http://localhost:8080/fhir-server-api-mvn/a/a/Patient/1000000005","Get particular patient");
   var ds3=new DataStore("http://localhost:8080/fhir-server-api-mvn/a/a/MedicationStatement?patient=1000000005","Get Medication Statements for a particular patient");
   var ds4=new DataStore("http://localhost:8080/fhir-server-api-mvn/a/a/MedicationStatement?patient=1000000005&_include=MedicationStatement.Medication&_include=MedicationStatement.Patient","Get Medication Statements for a particular patient and include Medications and the Patient");
   
   DSArray.push(ds1);
   DSArray.push(ds2);
   DSArray.push(ds3);
   DSArray.push(ds4);
   
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
    