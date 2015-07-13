function httpGet(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}


var demo = {
   //serviceUrl: "http://52.2.43.105:8080/srv-dstu1-0.2/api",
    //patientId: "1000000005"
   serviceUrl: "http://localhost:8080/srv-dstu1-0.2/api",
    patientId: "1000000005"
    
   // serviceUrl: "https://fhir-open-api.smarthealthit.org",
   // patientId: "1137192"
   //serviceUrl: "http://fhirtest.uhn.ca/baseDstu1",
	//patientId: "19324"
   
};

// Create a FHIR client (server URL, patient id in `demo`)
var smart = FHIR.client(demo),
    pt = smart.context.patient;
pt.read();
document.getElementById("display").innerHTML =JSON.stringify(pt,null,4);
// Create a patient banner by fetching + rendering demographics
pt.read()
    .then(function (p) {
    //var name = p.name[0];
    //var formatted = name.given.join(" ") + " " + name.family;
    //$("#patient_name").text(formatted);
     document.getElementById("display").innerHTML = JSON.stringify(p,null,4);
});

// A more advanced query: search for active Prescriptions, including med details

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}
//sleep(10000);
pt.MedicationPrescription.where//.status("active")
    //._include("MedicationPrescription.medication")
    .search()
    .then(function (prescriptions) {
    prescriptions.forEach(function (rx) {
        var med = smart.cachedLink(rx, rx.medication);
        var row = $("<li> " + med.name + "</li>");
        $("#med_list").append(row);
    });
});

