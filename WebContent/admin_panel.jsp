<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="admin	panel" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>
	Hello,
	<c:out value="${user.firstName} ${user.lastName}" />
	<nav class="nav flex-column">
		<a class="nav-link active"
			href="controller?command=order&action=allOrders">Orders
			list</a> <a class="nav-link" href="#">Settings</a>
			<a class="nav-link" href="controller?command=cabinet&action=productsList">Products list</a>
			<a class="nav-link" href="controller?command=cabinet&action=categoryList">Catagories list</a>
			<a class="nav-link" href="controller?command=cabinet&action=usersList">Users list</a>
	</nav>
	



	<!--Section: Products v.3-->

	<!--Pagination-->

	<!--Pagination-->



</body>
</html>