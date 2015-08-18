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
	document.getElementById("display").innerHTML="loading...";
	
	$("#demo_list").empty().attr("frame","none");
	$("#med_list").empty().attr("frame","none");
	$("#lab_list").empty().attr("frame","none");
	$("#diag_list").empty().attr("frame","none");

	var patientIdInput=$("#patientId").val();
	var demo = null;
	demo={
		 //serviceUrl: "http://localhost:8080/srv-dstu2-0.2/api/open",
		 //patientId: "1000000005"
		serviceUrl : "https://fhir-open-api-dstu2.smarthealthit.org",
		 patientId: "1098667"
		 //patientId : patientIdInput

	//	// serviceUrl: "http://fhirtest.uhn.ca/baseDstu1",
	// patientId: "19324"

	};
	// Create a FHIR client (server URL, patient id in `demo`)
	var smart = FHIR.client(demo);
	var pt = null;
	pt=smart.context.patient;

	pt.read().then(function(p) {
	var row = $("<tr>");
	document.getElementById("display").innerHTML=JSON.stringify(p,null,4);
	row.append( '<tr><td>Gender:</td><td>'+p.gender+'</td></tr>');
	row.append( '<tr><td>BirthDate:</td><td>'+p.birthDate.split("T")[0]+'</td></tr>');
	row.append( '<tr><td>MaritalStatus:</td><td>'+p.maritalStatus+'</td></tr>');
	$('#demo_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
	$("#demo_list").append('<th align="left"">Demographics</th>');
	$("#demo_list").append(row);
	
	});
	

	/*
	pt.MedicationPrescription.where// .status("active")
	//._include("MedicationStatement.medication")
	//._include("MedicationStatement.patient")
	.search().then(
			function(prescriptions) {
				document.getElementById("display").innerHTML=JSON.stringify(prescriptions,null,4);
				
			//	document.getElementById("display").innerHTML=JSON.stringify(prescriptions[0],null,4);
					var rx=prescriptions[0];
				var p= smart.cachedLink(rx, rx.patient);
				
				
				$('#med_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#med_list").append('<th align="left">Medications</th>');

				$('#lab_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#lab_list").append('<th align="left">Labs</th>');
				
				$('#diag_list').attr("cellspacing", 5).attr("cellpadding", 0).attr("frame","box");
				$("#diag_list").append('<th align="left">Diagnosis:</th>');
					//document.getElementById("display").innerHTML =JSON.stringify(rx, null, 4);
					
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
	*/
	pt.Observation.where// .status("active")
	//._include("Observation.subject")
	.search().then(
	
			function(observations) {
				document.getElementById("display1").innerHTML=JSON.stringify(observations[0],null,4);
			
				observations.forEach(function(lab) {
				var row = $("<tr>");
				row.append( $("<td>").text(lab.appliesPeriod.start.replace("T"," ")));
				
				var labName="--";
				if(lab.hasOwnProperty("name")){	
						labName=lab.name.coding[0].display;
				}
				if(labName==null) {labName="--";}
				
				row.append( $("<td>").text(labName));
				var val="";	var units="";	
				if(lab.hasOwnProperty("valueQuantity") && lab.valueQuantity.hasOwnProperty("value")){
					val=lab.valueQuantity.value;
					units=lab.valueQuantity.units;
				}
				if(lab.hasOwnProperty("valueCodeableConcept")  ){
					val=lab.valueCodeableConcept.coding[0].code;
					//units=lab.valueQuantity.units;
				}
				row.append( $("<td>").text(val+"  "+units));
				//row.append( $("<td>").text(units));
				
				$("#lab_list").append(row);
				
				});
				document.getElementById("display").innerHTML="";
			
				}
	);
	/*
	pt.Condition.where// .status("active")
	//._include("Observation.subject")
	.search().then(
	
			function(conditions) {
				//document.getElementById("display1").innerHTML=JSON.stringify(conditions[0],null,4);
			
				conditions.forEach(function(cond) {
				var row = $("<tr>");
				row.append( $("<td>").text(cond.dateAsserted.replace("T"," ")));
				
				var condName="--";
				if(cond.hasOwnProperty("code")){	
						condName=cond.code.coding[0].code;
				}
				if(condName==null) {condName="--";}
				
				row.append( $("<td>").text(condName));
			
				$("#diag_list").append(row);
			
				});
				document.getElementById("display").innerHTML="";
			
				}
	);
*/	
	
	
	
}

$('#med_list').find('*').each(function() {
     var tmp = $(this).children().remove(); //removing and saving children to a tmp obj
    var text = $(this).text(); //getting just current node text
    text = text.replace(/undefined/g, "marshmellows"); //replacing every lollypops occurence with marshmellows
    $(this).text(text); //setting text
    $(this).append(tmp); //re-append 'foundlings'
});

$( document ).ready(function() {
	document.getElementById("display").innerHTML="loading...";
	updatePatientDisplay();
});
