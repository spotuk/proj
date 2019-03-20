<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="main" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>
	<div class="container row">
		<div class="container row">
			<div class="btn-group-vertical">
				Categories:
				<c:forEach items="${categoriesList}" var="category">
					<a class="btn btn-outline-success"
						href="controller?command=category&action=category&categoryId=${category.id}"
						role="button">${category.title}</a>

				</c:forEach>
			</div>
			<form method="post" action="controller">
				<input type="hidden" name="command" value="category" />
				<input type="hidden" name="action" value="category" />

				<div class="form-group col-md-4">
					<label for="choosedBrand">Brand</label> <select name="choosedBrand"
						id="choosedBrand" class="form-control">
						<option selected value="">Choose...</option>
						<c:forEach items="${brandList}" var="brand">
							<option value="${brand.brand}">${brand.brand}</option>
						</c:forEach>

					</select>
				</div>
				<div class="form-group col-md-4">
					<label for="sortType">Sort by</label> <select id="sortType"
						name="sortType" class="form-control">
						<option selected value="">Choose...</option>
						<option value="title ASC">Alphabet(A-Z)</option>
						<option value="title DESC">Alphabet(Z-A)</option>
						<option value="price ASC">By price from min to max</option>
						<option value="price DESC">By price from max to min</option>
						<option value="date_added ASC">newer products</option>
						<option value="date_added DESC">older products</option>
					</select>
				</div>



				Price in range
				<div class="row">

					<div class="col">
						<input type="text" class="form-control" placeholder="from"
							name="priceFrom">
					</div>
					<div class="col">
						<input type="text" class="form-control" placeholder="to"
							name="priceTo">
					</div>
					<input type="submit">
				</div>

			</form>
		</div>

	</div>


	<main>
	<div class="container">

		<!--Navbar-->

		<!--/.Navbar-->

		<!--Section: Products v.3-->
		<section class="text-center mb-4">
			<!--Grid row-->
			<div class="row wow fadeIn">

				<c:forEach items="${products}" var="product">


					<!--Grid column-->
					<div class="col-lg-3 col-md-6 mb-4 " style="min-height: 600px;">
						<!--Card-->
						<div class="card" style="min-height: 600px;">
							<!--Card image-->
							<div class="view overlay">

								<img src="${product.img}" class="card-img-top" alt="">
							</div>
							<!--Card image-->

							<!--Card content-->
							<div class="card-body text-center">
								<!--Category & Title-->

								<h5>

									<a href="controller?command=product&action=product&productId=${product.id}">${product.title}

										&ensp;${product.brand}</a>
								</h5>

								<h5>
									<strong> ${product.description} </strong>
								</h5>

								<h4 class="font-weight-bold blue-text">
									<strong>${product.price} uah</strong>
								</h4>




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
							<!--Card content-->

						</div>

						<!--Card-->

					</div>

				</c:forEach>

				<!--Grid column-->
			</div>


			<!--Grid column-->
		</section>


		<!--Section: Products v.3-->

		<!--Pagination-->
		
		<!--Pagination-->

	</div>
	</main>


</body>
</html>