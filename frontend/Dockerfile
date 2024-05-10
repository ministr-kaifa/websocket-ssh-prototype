FROM node:20
WORKDIR /app
COPY package.json ./
RUN yarn 
COPY . .
EXPOSE 5173
CMD ["yarn", "dev"]
