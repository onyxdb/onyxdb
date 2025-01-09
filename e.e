db.createUser({
    user: 'user',
    pwd: 'password',
    roles: [{ role: 'dbOwner', db:'admin'}, { role: 'dbOwner', db:'sample-db'}]
});