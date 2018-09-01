<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Optical Character Recognition</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/ocr.css">
</head>
<body>
	<div class="container">
		<header>
		<div class="header-title">
			<h2>Optical Character Recognition</h2>
		</div>
		</header>

		<div class="jumbotron">
			<center>
				<h3>
					<span class="text-danger">${emptyMsg}</span> <span
						class="text-success">${successMsg}</span>
				</h3>
			</center>
			<form:form method="post"
				action="${pageContext.request.contextPath}/process"
				enctype="multipart/form-data">

				<div class="row">
					<div class="col-md-5">
						<div class="form-group input">

							<input type="file" name="file" id="fileToUpload"> <br>

							<p class="help-block">Upload the image file to apply the ocr</p>

						</div>
					</div>
					<div class="col-md-5">
						<select class="form-control" name="extension">
							<option value="docx">.docx</option>
							<option value="pdf">.pdf</option>
							<option value="txt">.txt</option>
						</select>
						<p class="help-block">Choose the format</p>
					</div>
					<div class="col-md-2">

						<button type="submit" class="btn btn-default btn-lg">Process</button>
					</div>
				</div>

			</form:form>

			<hr>
			<span> <c:forEach var="imgname" items="${image}">
					<div class="row">
						<div class="col-md-4">
							<a href="resources/images/${imgname}" target="_blank"> <img
								alt="" src="resources/images/${imgname}" class="img-responsive">
							</a>
						</div>
						<div class="col-md-4">
							<c:if test="${requestScope.pram ne null }">
								<a href="resources/images/gray.jpg" target="_blank"> <img
									alt="" src="resources/images/gray.jpg" class="img-responsive">
								</a>
							</c:if>
						</div>
						<div class="col-md-4">
							<c:forEach var="segments" items="${segment}">
								<p>${segments}</p>
							</c:forEach>
						</div>
					</div>
				</c:forEach> <br>


			</span>
		</div>
	</div>
</body>
</html>


<%-- <c:if test="${requestScope.image ne null }">
					<a href="resources/images/${image}" target="_blank"> <img
						alt="" src="resources/images/${image}" class="img-responsive">
					</a>
</c:if> --%>