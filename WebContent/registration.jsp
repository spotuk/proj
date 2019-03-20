<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="Registration" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<div class="container mt-5">
		<form id="register_form" action="controller" method="post"
			xmlns="http://www.w3.org/1999/html">
			<input type="hidden" name="command" value="registration" />

			<div class="form-group row">
				<label class="col-sm-2 col-form-label">User Login:</label>
				<div class="col-sm-4">
					<input type="text" name="login" class="form-control"
						placeholder="User Login" required />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 col-form-label">User Sername:</label>
				<div class="col-sm-4">
					<input type="text" name="userSername" class="form-control"
						placeholder="User Sername" required />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 col-form-label">User Name:</label>
				<div class="col-sm-4">
					<input type="text" name="userName" class="form-control"
						placeholder="User name" required />
				</div>
			</div>
			<div class="form-group  row">
				<label class="col-sm-2 col-form-label">Password:</label>
				<div class="col-sm-4">
					<input type="password" name="password" class="form-control"
						placeholder="Password" id="password1" required />
				</div>
			</div>
			<div class="form-group  row">
				<label class="col-sm-2 col-form-label">Password:</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" placeholder="Password"
						id="password2" required />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 col-form-label">Email:</label>
				<div class="col-sm-4">
					<input type="email" name="email" class="form-control"
						placeholder="some@some.com" required />
				</div>

			</div>
			<div class="row justify-content-md-center">


				<button type="submit" class="btn btn-primary mb-2">Register</button>
			</div>

		</form>
	</div>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("password1").onchange = validatePassword;
			document.getElementById("password2").onchange = validatePassword;
		}
		function validatePassword() {
			var pass2 = document.getElementById("password2").value;
			var pass1 = document.getElementById("password1").value;
			if (pass1 != pass2)
				document.getElementById("password2").setCustomValidity(
						"Passwords Don't Match");
			else
				document.getElementById("password2").setCustomValidity('');
			//empty string means no validation error
		}
	</script>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>