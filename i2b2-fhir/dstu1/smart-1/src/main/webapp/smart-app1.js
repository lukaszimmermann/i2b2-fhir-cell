function httpGet(theUrl) {
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("GET", theUrl, false);
	xmlHttp.send(null);
	return xmlHttp.responseText;
}


$('#patientId').on('keyup', function () {
	//alert("key up"+$("#patientId").val());
  $("updatePatientInfo").html($("#patientId").val());
});

$("#target").submit(function(event) {
	updatePatientDisplay();
	//document.getElementById("display").innerHTML = $("#patientId").attr('value');//document.getElementById('patientId').value ;//JSON.stringify($("#patientId"), null, 4);
	event.preventDefault();
});

function updatePatientDisplay() {
	//alert("getting Patient Info");
	$("#med_list").empty();

	var patientIdInput=$("#patientId").val();
	var demo = null;
	demo={
		// serviceUrl: "http://52.2.43.105:8080/srv-dstu1-0.2/api",
		// patientId: "1000000005"
		serviceUrl : "http://localhost:8080/srv-dstu1-0.2/api",
		patientId : patientIdInput

	// serviceUrl: "https://fhir-open-api.smarthealthit.org",
	// patientId: "1137192"
	// serviceUrl: "http://fhirtest.uhn.ca/baseDstu1",
	// patientId: "19324"

	};
	// Create a FHIR client (server URL, patient id in `demo`)
	var smart = FHIR.client(demo);
	var pt = null;
	pt=smart.context.patient;

	pt.read();
	document.getElementById("display").innerHTML = JSON.stringify(pt, null, 4);
	// Create a patient banner by fetching + rendering demographics
	pt.read().then(
			function(p) {
				// var name = p.name[0];
				// var formatted = name.given.join(" ") + " " + name.family;
				// $("#patient_name").text(formatted);
				document.getElementById("display").innerHTML = JSON.stringify(
						p, null, 4);
				
				$("#gender").html(p.gender.coding[0].display);
				$("#birthDate").html(p.birthDate.split("T")[0]);
				$("#maritalStatus").html(p.maritalStatus.coding[0].display);
			});

	// A more advanced query: search for active Prescriptions, including med
	// details
	

	pt.MedicationPrescription.where// .status("active")
	._include("MedicationPrescription.medication").search().then(
			function(prescriptions) {
				prescriptions.forEach(function(rx) {
					var med = smart.cachedLink(rx, rx.medication);
					var row = $("<li> " + med.name
					// JSON.stringify(med,null,4)
					+ "</li>");
					
					$("#med_list").append(row);
					// document.getElementById("display").innerHTML=JSON.stringify(med,null,4);
				});
			});

}
