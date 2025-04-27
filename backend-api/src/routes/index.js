const express = require('express');
const router = express.Router();
const authRoute = require('./auth.route');
const productRoute = require('./product.route');

const defaultRoutes = [
	{
		path: '/auth',
		route: authRoute,
	},
	{
		path: '/product',
		route: productRoute,
	},
];

defaultRoutes.forEach((route) => {
	router.use(route.path, route.route);
});

module.exports = router;
