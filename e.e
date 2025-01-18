db.createUser({
    user: 'admin',
    pwd: 'admin',
    roles: [{ role: 'dbOwner', db:'admin'}, { role: 'dbOwner', db:'sample-db'}]
});

rs.initiate(
 {
  _id: "rs0",
  members: [
   { _id: 0, host: "managed-mongodb-sample-db-0.managed-mongodb-sample-db.onyxdb:27017" },
   { _id: 1, host: "managed-mongodb-sample-db-1.managed-mongodb-sample-db.onyxdb:27017" },
   { _id: 2, host: "managed-mongodb-sample-db-2.managed-mongodb-sample-db.onyxdb:27017" }
  ]
 }
)