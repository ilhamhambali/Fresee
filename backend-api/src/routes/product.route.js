const express = require('express');
const productController = require('../controller/product.controller.js');

const router = express.Router();

router.route('/').get(productController.getAllProduct);
router.route('/:productId').get(productController.getProductDetail);

router.route('/:userId').get(productController.getUserProduct);
// router.route('/:userId').post(productController.login);

module.exports = router;
