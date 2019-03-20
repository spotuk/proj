<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<script class="jsbin" src="js/jquery.min.js"></script>
<script class="jsbin" src="js/jquery-ui.min.js"></script>


<c:set var="title" value="Registration" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
<div class="container mt-5">
	<c:choose>
		<c:when test="${type == 'create'}">
			<form action="controller?command=product&action=productCreate"
				method="post" enctype="multipart/form-data">
				<div class="form-group row">
					<label class="col-sm-2 col-form-label">Title:</label>
					<div class="col-sm-4">
						<input type="text" name="enProductTitle" class="form-control"
							placeholder="English"  required />
					</div>
					<div class="col-sm-4">
						<input type="text" name="ruProductTitle" class="form-control"
							placeholder="Russian" required />
					</div>
				</div>

				<div class="mb-4">
					<label for="validationTextarea">English description : </label>
					<textarea name="enProductDescription"
						class="form-control is-invalid" id="validationTextarea"
						placeholder="English description" cols="100" rows="3"
						maxlength="300" required></textarea>

				</div>
				<div class="mb-4">
					<label for="validationTextarea">Russian description :</label>
					<textarea name="ruProductDescription"
						class="form-control is-invalid" id="validationTextarea"
						placeholder="Russian description" cols="100" rows="3"
						maxlength="300" required></textarea>

				</div>
				<div class="form-group row">
					<label class="col-sm-2 col-form-label">Price:</label>
					<div class="col-sm-4">
						<input type="number" min="10" max="10000" step="10" name="price"
							class="form-control" placeholder="Price" required />
					</div>
					<div class="col-sm-4">
						<div class="custom-file">
							<input type="file" name="productImage" accept="image/*"
								onchange="readURL(this)" /> <img id="blah" src="#"
								alt="your image" />

						</div>
					</div>


				</div>
				<div class="form-group row">
					<label class="col-sm-2 col-form-label">Quantity:</label>
					<div class="col-sm-4">
						<input type="number" min="10" max="10000" step="10"
							name="quantity" class="form-control" placeholder="Quantity"
							required />
					</div>
				</div>
				<div class="form-group  row">
					<label class="col-sm-2 col-form-label">Brand:</label>
					<div class="col-sm-4">
						<input type="text" name="brand" class="form-control"
							placeholder="Brand" required />
					</div>
				</div>
				<div class="form-group  row">
					<div class="col-sm-4">
						<label for="categoryId">Category</label> <select id="sortType"
							name="categoryId" class="form-control" required>
							<option selected value=""></option>
							<c:forEach items="${categoriesList}" var="category">
								<option value="${category.id}">${category.title}</option>
							</c:forEach>
						</select>
					</div>
				</div>


				<div class="form-group  row">
					<div class="col-sm-4">
						<button type="submit" class="btn btn-primary mb-2">Create</button>
					</div>
				</div>

			</form>


		</c:when>
		<c:when test="${type == 'update'}">
			<div class="container mt-5">
				<form action="controller?command=product&action=productUpdate"
					method="post" enctype="multipart/form-data">


					<div class="form-group row">
						<label class="col-sm-2 col-form-label">Title:</label>
						<div class="col-sm-4">
							<input type="text" name="enProductTitle" class="form-control"
								placeholder="English" value ="${productDTO.enProductTitle}" required />
						</div>
						<div class="col-sm-4">
							<input type="text" name="ruProductTitle" class="form-control"
								placeholder="Russian" value ="${productDTO.ruProductTitle}" required />
						</div>
					</div>

					<div class="mb-4">
						<label for="validationTextarea">English description : </label>
						<textarea name="enProductDescription"
							class="form-control is-invalid" id="validationTextarea"
							placeholder="English description" cols="100" rows="3"
							maxlength="300" required>${productDTO.enProductDescription}</textarea>

					</div>
					<div class="mb-4">
						<label for="validationTextarea">Russian description :</label>
						<textarea name="ruProductDescription"
							class="form-control is-invalid" id="validationTextarea"
							placeholder="Russian description" cols="100" rows="3"
							maxlength="300"  required>${productDTO.ruProductDescription}</textarea>

					</div>
					<div class="form-group row">
						<label class="col-sm-2 col-form-label">Price:</label>
						<div class="col-sm-4">
							<input type="number" min="1" max="10000" value ="${productDTO.price}" name="price"
								class="form-control" placeholder="Price" required />
						</div>
						<div class="col-sm-4">
							<div class="custom-file">
								<input type="file" name="productImage"  accept="image/*"
									onchange="readURL(this)" /> <img id="blah" src="${productDTO.img}"
									alt="your image" />

							</div>
						</div>


					</div>
					<div class="form-group row">
						<label class="col-sm-2 col-form-label">Quantity:</label>
						<div class="col-sm-4">
							<input type="number" min="1" max="10000" value ="${productDTO.quantity}"
								name="quantity" class="form-control" placeholder="Quantity"
								required />
						</div>
					</div>
					<div class="form-group  row">
						<label class="col-sm-2 col-form-label">Brand:</label>
						<div class="col-sm-4">
							<input type="text" name="brand" class="form-control"
								placeholder="Brand" value ="${productDTO.brand}" required />
						</div>
					</div>
					<div class="form-group  row">
						<div class="col-sm-4">
							<label for="categoryId">Category</label> <select id="sortType"
								name="categoryId" class="form-control" required>
								<option selected value ="${productDTO.categoryId}">${category.title}</option>
								<c:forEach items="${categoriesList}" var="category">
									<option value="${category.id}">${category.title}</option>
								</c:forEach>
							</select>
						</div>
					</div>


					<div class="form-group  row">
						<div class="col-sm-4">
						<input type="hidden" name="productId"value ="${productId}" />
							<button type="submit" class="btn btn-primary mb-2">Update</button>
						</div>
					</div>


				</form>
			</div>


		</c:when>
	</c:choose>
	</div>
	<script>
		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#blah').attr('src', e.target.result).width(150).height(
							200);
				};

				reader.readAsDataURL(input.files[0]);
			}
		}
	</script>

</body>
</html>