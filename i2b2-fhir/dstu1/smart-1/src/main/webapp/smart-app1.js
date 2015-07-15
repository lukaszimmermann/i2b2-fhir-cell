function httpGet(theUrl) {
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("GET", theUrl, false);
	xmlHttp.send(null);
	return xmlHttp.responseText;
}


$('#patientId').on('keyup', function () {
	// alert("key up"+$("#patientId").val());
  $("updatePatientInfo").html($("#patientId").val());
});

$("#target").submit(function(event) {
	updatePatientDisplay();
	// document.getElementById("display").innerHTML =
	// $("#patientId").attr('value');//document.getElementById('patientId').value
	// ;//JSON.stringify($("#patientId"), null, 4);
	event.preventDefault();
});

function updatePatientDisplay() {
	// alert("getting Patient Info");
	$("#demo_list").empty().attr("frame","none");
	$("#med_list").empty().attr("frame","none");

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

	//pt.read();
	
	pt.MedicationPrescription.where// .status("active")
	._include("MedicationPrescription.medication")
	._include("MedicationPrescription.patient").search().then(
			function(prescriptions) {
				//document.getElementById("display").innerHTML=JSON.stringify(prescriptions[0],null,4);
				var rx=prescriptions[0];
				var p= smart.cachedLink(rx, rx.patient);
				var row = $("<tr>");
				row.append( '<tr><td>Gender:</td><td>'+p.gender.coding[0].display+'</td></tr>');
				row.append( '<tr><td>BirthDate:</td><td>'+p.birthDate.split("T")[0]+'</td></tr>');
				row.append( '<tr><td>MaritalStatus:</td><td>'+p.maritalStatus.coding[0].display+'</td></tr>');
				$('#demo_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#demo_list").append('<th>Demographics</th>');
				$("#demo_list").append(row);
				
				$('#med_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
					$("#med_list").append('<th >Medications</th>');
					document.getElementById("display").innerHTML =JSON.stringify(rx, null, 4);
					
				prescriptions.forEach(function(rx) {
					var med = smart.cachedLink(rx, rx.medication);
					var row = $("<tr>");
					row.append( $("<td>").text(rx.dateWritten.split("T")[0]));
					row.append( $("<td>").text(med.name ));
					var di=rx.dosageInstruction[0];
					row.append( $("<td>").text(di.timingSchedule.repeat.frequency+"/"+di.timingSchedule.repeat.units));
					//row.append( $("<td>").text(di.doseQuantity.value+" "+di.doseQuantity.units));
					// $("#med_list").append('<tr><td>'+rx.dateWritten.split("T")[0]+'</td><td>'+med.name+'</td></tr>');
					$("#med_list").append(row);
					
					
				});
				
			
		});
}

$('#med_list').find('*').each(function() {
     var tmp = $(this).children().remove(); //removing and saving children to a tmp obj
    var text = $(this).text(); //getting just current node text
    text = text.replace(/undefined/g, "marshmellows"); //replacing every lollypops occurence with marshmellows
    $(this).text(text); //setting text
    $(this).append(tmp); //re-append 'foundlings'
});

$( document ).ready(function() {
	updatePatientDisplay();
});
