require("dotenv").config();

const express = require("express");
const { PrismaClient } = require("@prisma/client");

const app = express();
const prisma = new PrismaClient();

app.use(express.json());

app.get("/", (req, res) => {
  res.json({
    message: "LB4 simple online shop ORM API is running"
  });
});

/* USERS */

app.get("/api/users", async (req, res) => {
  const users = await prisma.user.findMany({
    include: {
      orders: true,
      reviews: true
    }
  });

  res.json(users);
});

app.post("/api/users", async (req, res) => {
  const { name, email } = req.body;

  const user = await prisma.user.create({
    data: {
      name,
      email
    }
  });

  res.status(201).json(user);
});

app.put("/api/users/:id", async (req, res) => {
  const id = Number(req.params.id);
  const { name, email } = req.body;

  const user = await prisma.user.update({
    where: { id },
    data: {
      name,
      email
    }
  });

  res.json(user);
});

app.delete("/api/users/:id", async (req, res) => {
  const id = Number(req.params.id);

  await prisma.user.delete({
    where: { id }
  });

  res.json({
    message: "User deleted"
  });
});

/* CATEGORIES */

app.get("/api/categories", async (req, res) => {
  const categories = await prisma.category.findMany({
    include: {
      products: true
    }
  });

  res.json(categories);
});

app.post("/api/categories", async (req, res) => {
  const { name, description } = req.body;

  const category = await prisma.category.create({
    data: {
      name,
      description
    }
  });

  res.status(201).json(category);
});

app.put("/api/categories/:id", async (req, res) => {
  const id = Number(req.params.id);
  const { name, description } = req.body;

  const category = await prisma.category.update({
    where: { id },
    data: {
      name,
      description
    }
  });

  res.json(category);
});

app.delete("/api/categories/:id", async (req, res) => {
  const id = Number(req.params.id);

  await prisma.category.delete({
    where: { id }
  });

  res.json({
    message: "Category deleted"
  });
});

/* PRODUCTS */

app.get("/api/products", async (req, res) => {
  const products = await prisma.product.findMany({
    include: {
      category: true,
      orders: true,
      reviews: true
    }
  });

  res.json(products);
});

app.post("/api/products", async (req, res) => {
  const { title, description, price, stock, categoryId } = req.body;

  const product = await prisma.product.create({
    data: {
      title,
      description,
      price: Number(price),
      stock: Number(stock),
      categoryId: Number(categoryId)
    }
  });

  res.status(201).json(product);
});

app.put("/api/products/:id", async (req, res) => {
  const id = Number(req.params.id);
  const { title, description, price, stock, categoryId } = req.body;

  const product = await prisma.product.update({
    where: { id },
    data: {
      title,
      description,
      price: Number(price),
      stock: Number(stock),
      categoryId: Number(categoryId)
    }
  });

  res.json(product);
});

app.delete("/api/products/:id", async (req, res) => {
  const id = Number(req.params.id);

  await prisma.product.delete({
    where: { id }
  });

  res.json({
    message: "Product deleted"
  });
});

/* ORDERS */

app.get("/api/orders", async (req, res) => {
  const orders = await prisma.order.findMany({
    include: {
      user: true,
      products: true
    }
  });

  res.json(orders);
});

app.post("/api/orders", async (req, res) => {
  const { userId, productIds, status } = req.body;

  const products = await prisma.product.findMany({
    where: {
      id: {
        in: productIds.map(Number)
      }
    }
  });

  const total = products.reduce((sum, product) => {
    return sum + product.price;
  }, 0);

  const order = await prisma.order.create({
    data: {
      userId: Number(userId),
      total,
      status: status || "NEW",
      products: {
        connect: productIds.map((id) => ({
          id: Number(id)
        }))
      }
    },
    include: {
      user: true,
      products: true
    }
  });

  res.status(201).json(order);
});

app.put("/api/orders/:id", async (req, res) => {
  const id = Number(req.params.id);
  const { userId, productIds, status } = req.body;

  const products = await prisma.product.findMany({
    where: {
      id: {
        in: productIds.map(Number)
      }
    }
  });

  const total = products.reduce((sum, product) => {
    return sum + product.price;
  }, 0);

  const order = await prisma.order.update({
    where: { id },
    data: {
      userId: Number(userId),
      total,
      status,
      products: {
        set: productIds.map((productId) => ({
          id: Number(productId)
        }))
      }
    },
    include: {
      user: true,
      products: true
    }
  });

  res.json(order);
});

app.delete("/api/orders/:id", async (req, res) => {
  const id = Number(req.params.id);

  await prisma.order.delete({
    where: { id }
  });

  res.json({
    message: "Order deleted"
  });
});

/* REVIEWS */

app.get("/api/reviews", async (req, res) => {
  const reviews = await prisma.review.findMany({
    include: {
      user: true,
      product: true
    }
  });

  res.json(reviews);
});

app.post("/api/reviews", async (req, res) => {
  const { userId, productId, rating, comment } = req.body;

  const review = await prisma.review.create({
    data: {
      userId: Number(userId),
      productId: Number(productId),
      rating: Number(rating),
      comment
    }
  });

  res.status(201).json(review);
});

app.put("/api/reviews/:id", async (req, res) => {
  const id = Number(req.params.id);
  const { userId, productId, rating, comment } = req.body;

  const review = await prisma.review.update({
    where: { id },
    data: {
      userId: Number(userId),
      productId: Number(productId),
      rating: Number(rating),
      comment
    }
  });

  res.json(review);
});

app.delete("/api/reviews/:id", async (req, res) => {
  const id = Number(req.params.id);

  await prisma.review.delete({
    where: { id }
  });

  res.json({
    message: "Review deleted"
  });
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});