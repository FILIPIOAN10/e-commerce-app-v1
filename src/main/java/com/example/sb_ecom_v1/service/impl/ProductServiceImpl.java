package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.APIException;
import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Cart;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.model.Product;
import com.example.sb_ecom_v1.payload.CartDTO;
import com.example.sb_ecom_v1.payload.ProductDTO;
import com.example.sb_ecom_v1.payload.ProductResponse;
import com.example.sb_ecom_v1.repository.CartRepository;
import com.example.sb_ecom_v1.repository.CategoryRepository;
import com.example.sb_ecom_v1.repository.ProductRepository;
import com.example.sb_ecom_v1.service.CartService;
import com.example.sb_ecom_v1.service.FileService;
import com.example.sb_ecom_v1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    private final CartRepository cartRepository;
    private final CartService cartService;


    @Value("${project.image}")
    private String path;
    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {


        // fetch the category from db -> throw exception if not found
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // check if product is already present or not
        boolean ifProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                ifProductNotPresent = false;
                break;
            }
        }
        if (ifProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else throw new APIException("Product already exists !!!");
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder =sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                            Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);
        List<Product> products= pageProducts.getContent();
        
        //Transformation of list of products in product
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        // Get the category from the database or throw ResourceNotFoundException
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sortByAndOrder =sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products= pageProducts.getContent();
        if(products.size() == 0){
            throw new APIException(category.getCategoryName()+" category does not have any products");
        }
        // Find all products that belong to that category

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder =sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
        List<Product> products= pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        if(products.size() == 0){
            throw new APIException("Products not found with keyword "+keyword);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO,Long productId) {
        // Get the existing product from DB
        // Fetch existing product from DB
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Update the product info with the one in the request body
        //automatically converts (maps) the productDTO object into a Product object.
        //Map incomming DO

        // apply update filed onto the managed DB entity
        Product product = modelMapper.map(productDTO,Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setSpecialPrice(product.getSpecialPrice());
        // Save to database
        // Persist updated product
        Product savedProduct = productRepository.save(productFromDB);

        List<Cart> carts   = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p->modelMapper.map(p.getProduct(), ProductDTO.class))
                    .toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();

        cartDTOS.forEach(cart->cartService.updateProductInCarts(cart.getCartId(),productId));

        return modelMapper.map(savedProduct,ProductDTO.class);


    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        //Fetch product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        List<Cart> carts   = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart-> cartService.deleteProductFromCart(cart.getCartId(),productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //  Get the product from db
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload the image to server /image directory

        String fileName = fileService.uploadImage(path,image);
        // get the file name of uploaded image
        // updating the new file name to the product

        productFromDb.setImage(fileName);
        // Save updated product
        Product updatedProduct = productRepository.save(productFromDb);
        // return DTO after mapping  product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }


}
