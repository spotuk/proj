package ua.kpi.getman.InternetShop.web.command;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import ua.kpi.getman.InternetShop.Path;
import ua.kpi.getman.InternetShop.db.DBManager;
import ua.kpi.getman.InternetShop.dto.ProductDTO;
import ua.kpi.getman.InternetShop.exception.AppException;
import ua.kpi.getman.InternetShop.exception.DBException;

public class ProductCommand extends Command {
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		CategoryCommand.languages(request);
		String forward = Path.PAGE_PRODUCT;
		String strAction = request.getParameter("action");

		switch (strAction) {
		case "product":

			product(request);
			forward = Path.PAGE_PRODUCT;
			break;

		case "productDelete":
			productDelete(request);
			forward = Path.PAGE_ADMIN_PANEL;
			break;
		case "productCreate":
			try {
				productCreate(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			forward = Path.PAGE_ADMIN_PANEL;
			break;
		case "productUpdate":
			try {
				productUpdate(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			forward = Path.PAGE_ADMIN_PANEL;
			break;

		case "productDirect":
			getCategories(request);
			String type = request.getParameter("type");
			request.setAttribute("type", type);

			if ("update".equals(type)) {
				productValuesForUpdate(request);
			}

			forward = Path.PAGE_ADMIN_PRODUCT_CREATE;
			break;
		}

		response.setContentType("text/html;charset=utf-8");

		LOG.debug("Command finished");
		return forward;
	}

	protected void product(HttpServletRequest request) throws DBException {
		String productId = request.getParameter("productId");
		LOG.trace("Request parameter: productId --> " + productId);

		DBManager manager = DBManager.getInstance();

		request.setAttribute("product",
				manager.findProductById(Long.valueOf(productId), CategoryCommand.localization(request)));
	}

	protected void productDelete(HttpServletRequest request) throws DBException {
		String productId = request.getParameter("productId");
		LOG.trace("Request parameter: productId --> " + productId);

		DBManager manager = DBManager.getInstance();
		if (productId != null && !productId.isEmpty())
			manager.deleteProductById(Long.valueOf(productId));
	}

	protected void productUpdate(HttpServletRequest request) throws Exception {
		String productId = request.getParameter("productId");
		ProductDTO prodForCreate = extractToProductDTO(request);
		
		DBManager manager = DBManager.getInstance();

		manager.updateProductById(Long.valueOf(productId),prodForCreate);

		

	}

	protected void productCreate(HttpServletRequest request) throws Exception {
		
		DBManager manager = DBManager.getInstance();
		
		ProductDTO prodForCreate = extractToProductDTO(request);
		
		manager.createProduct(prodForCreate);

	}
	private ProductDTO extractToProductDTO(HttpServletRequest request) throws Exception {
		ProductDTO prodForCreate = new ProductDTO();

		String ruProductTitle = request.getParameter("ruProductTitle");

		String enProductTitle = request.getParameter("enProductTitle");

		String ruProductDescription = request.getParameter("ruProductDescription");

		String enProductDescription = request.getParameter("enProductDescription");

		String price = request.getParameter("price");
		
		String img = uploadFileGetPath(request);

		String quantity = request.getParameter("quantity");

		String brand = request.getParameter("brand");

		String categoryId = request.getParameter("categoryId");

		prodForCreate.setBrand(brand);
		prodForCreate.setEnProductDescription(enProductDescription);
		prodForCreate.setEnProductTitle(enProductTitle);
		prodForCreate.setImg(img);
		prodForCreate.setPrice(Float.valueOf(price));
		prodForCreate.setQuantity(Integer.valueOf(quantity));
		prodForCreate.setRuProductDescription(ruProductDescription);
		prodForCreate.setRuProductTitle(ruProductTitle);
		prodForCreate.setCategoryId(Long.valueOf(categoryId));
		
		LOG.debug("ruProductName ==> " + ruProductTitle);
		LOG.debug("enProductName ==> " + enProductTitle);
		LOG.debug("ruProductDescription ==> " + ruProductDescription);
		LOG.debug("img ==> " + img);
		LOG.debug("quantity ==> " + quantity);
		LOG.debug("enProductDescription ==> " + enProductDescription);
		LOG.debug("brand ==> " + brand);
		LOG.debug("price ==> " + price);
		LOG.debug("categoryId ==> " + categoryId);
		
		return prodForCreate;

	}

	protected void productValuesForUpdate(HttpServletRequest request) throws DBException {
		String productId = request.getParameter("productId");
		LOG.debug("productId ==> " + productId);
		DBManager manager = DBManager.getInstance();
		ProductDTO product = manager.findProductForUpdateById(Long.valueOf(productId));
		
		request.setAttribute("productDTO", product);
		request.setAttribute("productId", productId);
		request.setAttribute("category", manager.findCategoryNameByIdAndLangId(CategoryCommand.localization(request),product.getCategoryId()));
		request.setAttribute("productId", productId);
	}



	private String uploadFileGetPath(HttpServletRequest request) throws IOException, ServletException {
		String applicationPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String filePath = null;
		String uploadFilePath = applicationPath + File.separator + Path.UPLOAD_DIR;

		// creates the save directory if it does not exists
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		LOG.trace("Upload File Directory=" + fileSaveDir.getAbsolutePath());

		// Get all the parts from request and write it to the file on server
		Part part = request.getPart("productImage");
			if ("productImage".equals(part.getName())) {
				String uuidFile = UUID.randomUUID().toString();
				String fileName = uuidFile + part.getSubmittedFileName();

				part.write(uploadFilePath + File.separator + fileName);
				LOG.trace("Request parameter: fileName --> " + fileName);
				LOG.trace("Request parameter: getFileName(part) --> " + part.getName());
				LOG.trace("Request parameter: getFileName(part) --> " + part.getSubmittedFileName());

				filePath = fileName;
			
		}
		return Path.UPLOAD_DIR + "/" + filePath;
	}

	protected void getCategories(HttpServletRequest request) throws AppException {
		CategoryCommand.allCategories(request, CategoryCommand.localization(request));
	}
}
