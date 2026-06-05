const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

async function main() {
  await prisma.review.deleteMany();
  await prisma.order.deleteMany();
  await prisma.product.deleteMany();
  await prisma.category.deleteMany();
  await prisma.user.deleteMany();

  const user1 = await prisma.user.create({
    data: {
      name: "Ivan Petrenko",
      email: "ivan@example.com"
    }
  });

  const user2 = await prisma.user.create({
    data: {
      name: "Olena Kovalenko",
      email: "olena@example.com"
    }
  });

  const category1 = await prisma.category.create({
    data: {
      name: "Electronics",
      description: "Electronic devices"
    }
  });

  const category2 = await prisma.category.create({
    data: {
      name: "Accessories",
      description: "Useful accessories"
    }
  });

  const product1 = await prisma.product.create({
    data: {
      title: "Smartphone",
      description: "Modern smartphone",
      price: 500,
      stock: 10,
      categoryId: category1.id
    }
  });

  const product2 = await prisma.product.create({
    data: {
      title: "Headphones",
      description: "Wireless headphones",
      price: 100,
      stock: 25,
      categoryId: category2.id
    }
  });

  await prisma.order.create({
    data: {
      userId: user1.id,
      total: product1.price + product2.price,
      status: "NEW",
      products: {
        connect: [
          { id: product1.id },
          { id: product2.id }
        ]
      }
    }
  });

  await prisma.review.create({
    data: {
      userId: user2.id,
      productId: product1.id,
      rating: 5,
      comment: "Good product"
    }
  });

  console.log("Seed data created successfully");
}

main()
  .catch((error) => {
    console.error(error);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });