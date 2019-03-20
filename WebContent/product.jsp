<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="product info" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>

	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<!-- Navbar -->

	<!--Main layout-->
	<main class="mt-5 pt-4">
	<div class="container dark-grey-text mt-5">

		<!--Grid row-->
		<div class="row wow fadeIn">

			<!--Grid column-->
			<div class="col-md-6 mb-4">

				<img src="${product.img}" class="img-fluid" alt="">

			</div>
			<!--Grid column-->

			<!--Grid column-->
			<div class="col-md-6 mb-4">

				<!--Content-->
				<div class="p-4">
					<p class="lead">
						<span class="mr-1"> <del>${product.price + (product.price * 0.2)}
								&ensp; uah</del> <br />
						</span> <span>${product.price} &ensp; uah</span>
					</p>

					<p class="lead font-weight-bold">Description</p>
					${product.description}

					<!-- Default input -->

					<form name="modelDetail1" method="POST" action="controller">
									<input type="text" size="2" value="1" name="quantity" /> <input
										type="hidden" name="productId" value="${product.id}" /> <input
										type="hidden" name="modelNo" value="${product.title}" /> <input
										type="hidden" name="price" value="${product.price}" /> <input
										type="hidden" name="description"
										value="${product.description}" /> <input type="hidden"
										name="command" value="cart" /> <input type="hidden"
										name="action" value="add" /> <input type="submit"
										name="addToCart" value="Add To Cart">
								</form>




				</div>
				<!--Content-->

			</div>
			<!--Grid column-->

		</div>
		<!--Grid row-->

		<hr>

		<!--Grid row-->

		<!--/.Footer-->

		<!-- SCRIPTS -->
		<!-- JQuery -->
		<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
		<!-- Bootstrap tooltips -->
		<script type="text/javascript" src="js/popper.min.js"></script>
		<!-- Bootstrap core JavaScript -->
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
		<!-- MDB core JavaScript -->
		<script type="text/javascript" src="js/mdb.min.js"></script>
		<!-- Initializations -->
		<script type="text/javascript">
			// Animations initialization
			new WOW().init();
		</script>
</body>

</html>