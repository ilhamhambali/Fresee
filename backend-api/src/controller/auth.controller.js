const prisma = require('../../prisma/index');
const bcrypt = require('bcrypt');
const ApiError = require('../utils/apiError');

const register = async (req, res, next) => {
	try {
		const email = await prisma.user.findFirst({
			where: {
				email: req.body.email,
			},
		});

		if (email) {
			throw new ApiError(401, 'Email already exits');
		}
		req.body.password = await bcrypt.hash(req.body.password, 10);

		const result = await prisma.user.create({
			data: {
				...req.body,
			},
		});

		res.status(200).json({
			status: 200,
			message: 'Success',
			data: result,
		});
	} catch (error) {
		next(error);
	}
};

const login = async (req, res, next) => {
	try {
		const email = await prisma.user.findFirst({
			where: {
				email: req.body.email,
			},
		});

		if (!email) {
			throw new ApiError(401, 'Email or password wrong');
		}

		const isPasswordValid = await bcrypt.compare(
			req.body.password,
			email.password
		);

		if (!isPasswordValid) {
			throw new ApiError(401, 'Email or password wrong');
		}

		res.status(200).json({
			status: 200,
			message: 'Success',
			data: email,
		});
	} catch (error) {
		next(error);
	}
};

module.exports = {
	register,
	login,
};
