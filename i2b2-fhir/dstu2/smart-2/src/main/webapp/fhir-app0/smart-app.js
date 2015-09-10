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
	event.preventDefault();
});

function updatePatientDisplay() {
	// alert("getting Patient Info");
	document.getElementById("display").innerHTML="loading...";
	
	$("#demo_list").empty().attr("frame","none");
	$("#med_list").empty().attr("frame","none");
	$("#lab_list").empty().attr("frame","none");
	$("#diag_list").empty().attr("frame","none");

	var patientIdInput=$("#patientId").val();
	var demo = null;

	FHIR.oauth2.ready(function(smart){
		
		var p=null;
		smart.api.Patient.where._id(patientIdInput).search().then(function(ptl){
			p=ptl[0];
		
			document.getElementById("display").innerHTML=JSON.stringify(p,null,4);
		
			var row = $("<tr>");
			// document.getElementById("display").innerHTML=JSON.stringify(p,null,4);
			row.append( '<tr><td>Gender:</td><td>'+p.gender+'</td></tr>');
			row.append( '<tr><td>BirthDate:</td><td>'+p.birthDate.split("T")[0]+'</td></tr>');
			row.append( '<tr><td>MaritalStatus:</td><td>'+p.maritalStatus.coding[0].display +'</td></tr>');
			$('#demo_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
			$("#demo_list").append('<th align="left"">Demographics</th>');
			$("#demo_list").append(row);
	
		
	
	
		
	p.MedicationPrescription.where// .status("active")
	._include("MedicationPrescription.Medication")
	//._include("MedicationPrescription.patient")
	.search().then(
			function(prescriptions) {
				
			//document.getElementById("display").innerHTML=JSON.stringify(prescriptions,null,4);
				//	var rx=prescriptions[0];
				//var p= smart.cachedLink(rx, rx.patient);
				
				
				$('#med_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#med_list").append('<th align="left">Medications</th>');

				$('#lab_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#lab_list").append('<th align="left">Labs</th>');
				
				$('#diag_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#diag_list").append('<th align="left">Diagnosis:</th>');
				//	document.getElementById("display").innerHTML =JSON.stringify(rx, null, 4);
					
				prescriptions.forEach(function(rx) {
					//document.getElementById("display").innerHTML=JSON.stringify(rx,null,4);
					var med = smart.cachedLink(rx, rx.medication);
					//document.getElementById("display").innerHTML=JSON.stringify(med,null,4);
						
				var name="-";
					if(med.hasOwnProperty("name")) name=med.name;
					var row = $("<tr>");
					var di=rx.dosageInstruction[0];
					//row.append( $("<td>").text(di.scheduledTiming.repeat.bounds.start));
					row.append( $("<td>").text(name ));
					
					//row.append( $("<td>").text(di.scheduledTiming.repeat.duration+"/"+di.scheduledTiming.repeat.durationUnits));
					//row.append( $("<td>").text(rx.dispense.quantity.value+" "+rx.dispense.quantity.units));
					// $("#med_list").append('<tr><td>'+rx.dateWritten.split("T")[0]+'</td><td>'+med.name+'</td></tr>');
					$("#med_list").append(row);
					
					
				});
			
			
		});
});
	
	
	
	});
}
	
	
$('#med_list').find('*').each(function() {
     var tmp = $(this).children().remove(); // removing and saving children to a
											// tmp obj
    var text = $(this).text(); // getting just current node text
    text = text.replace(/undefined/g, "marshmellows"); // replacing every
														// lollypops occurence
														// with marshmellows
    $(this).text(text); // setting text
    $(this).append(tmp); // re-append 'foundlings'
});


$( document ).ready(function() {
	document.getElementById("display").innerHTML="loading...";
	updatePatientDisplay();
});


