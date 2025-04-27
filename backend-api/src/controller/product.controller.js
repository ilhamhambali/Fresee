const prisma = require('../../prisma/index');
const bcrypt = require('bcrypt');
const ApiError = require('../utils/apiError');

const getAllProduct = async (req, res, next) => {
	try {
		return await prisma.product.findMany();
	} catch (error) {
		next(error);
	}
};

const getProductDetail = async (req, res, next) => {
	try {
		return await prisma.product.findFirst({
			where: {
				id: req.params.productId,
			},
		});
	} catch (error) {
		next(error);
	}
};

const getUserProduct = async (req, res, next) => {
	try {
		return await prisma.product.findMany({
			where: {
				userId: req.params.userId,
			},
		});
	} catch (error) {
		next(error);
	}
};

module.exports = {
	getAllProduct,
	getProductDetail,
	getUserProduct,
};
