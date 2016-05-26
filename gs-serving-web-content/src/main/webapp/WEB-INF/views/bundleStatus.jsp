<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
    <title>Bundle</title>
    <link rel="stylesheet" 
          type="text/css" 
          href="<c:url value="/resources/style.css" />" >
  </head>
  <body>
    <div class="bundleStatus">
      <div class="patient_id"><c:out value="${bundleStatus.patientId}" /></div>
      <div>
        <span class="bundle_status_level"><c:out value="${bundleStatus.bundleStatusLevel}" /></span>
      </div>
      <div>
        <span class="created_date"><c:out value="${bundleStatus.createdDate}" /></span>
      </div>
    </div>
  </body>
</html>