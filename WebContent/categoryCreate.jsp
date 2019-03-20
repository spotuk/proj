<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="category Create" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>

<c:choose>
		<c:when test="${type == 'create'}">
	<div class="container mt-5">
		<form id="categoryCreate" action="controller" method="post"
			xmlns="http://www.w3.org/1999/html">
			<input type="hidden" name="command" value="category" />
			<input type="hidden" name="action" value="categoryCreate" />

			<div class="form-group row">
				<label class="col-sm-2 col-form-label">Category name:</label>
				<div class="col-sm-4">
					<input type="text" name="ruCategoryName" class="form-control"
						placeholder="Russian name" required />
				</div>
				<div class="col-sm-4">
					<input type="text" name="enCategoryName" class="form-control"
						placeholder="English name" required />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 col-form-label">Sort Order:</label>
				<div class="col-sm-4">
					<input type="number"  min="10" max="10000" step="10" name="sortOrder" class="form-control"
						placeholder="Sort Order" required />
				</div>
			</div>

			<div class="row justify-content-md-center">
				<button type="submit" class="btn btn-primary mb-2">Create</button>
			</div>
		</form>
	</div>
	</c:when>
		<c:when test="${type == 'update'}">
			<div class="container mt-5">
				<form id="categoryCreate" action="controller" method="post"
					xmlns="http://www.w3.org/1999/html">
					<input type="hidden" name="command" value="category" /> <input
						type="hidden" name="action" value="categoryUpdate" /> <input
						type="hidden" name="categoryId" value="${categoryId}" />

					<div class="form-group row">
						<label class="col-sm-2 col-form-label">Category name:</label>
						<div class="col-sm-4">
							<input type="text" name="ruCategoryName" class="form-control"
								placeholder="Russian name"
								value="${categoryDTO.ruCategoryTitle}" required />
						</div>
						<div class="col-sm-4">
							<input type="text" name="enCategoryName" class="form-control"
								placeholder="English name"
								value="${categoryDTO.enCategoryTitle}" required />
						</div>
					</div>
					<div class="form-group row">
						<label class="col-sm-2 col-form-label">Sort Order:</label>
						<div class="col-sm-4">
							<input type="number" min="10" max="10000" step="10"
								name="sortOrder" class="form-control" placeholder="Sort Order"
								value="${categoryDTO.sortOrder}" required />
						</div>
					</div>

					<div class="row justify-content-md-center">
						<button type="submit" class="btn btn-primary mb-2">Update</button>
					</div>

				</form>
			</div>
		</c:when>
	</c:choose>
</body>
</html>