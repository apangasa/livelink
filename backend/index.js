require('dotenv').config()
const express = require('express')
const app = express()

app.use(express.json())

const admin = require('firebase-admin')
const serviceAccount = require('./service-account/livelink-eee25-663a463dc7d2.json')

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
})

const db = admin.firestore()

const port = process.env.PORT

const collection = db.collection('users')

app.get('/get', async (req, res) => {
    const id = req.query.id
    console.log(id)
    const dataRef = await db.collection('users').doc(id)

    const data = await dataRef.get()

    console.log(data.data())

    res.send(data.data())
})

app.post('/insert-profile', async (req, res) => {
    res.send('Hello World!')
    const id = req.query.id

    await collection.doc(id).set(req.body)
    console.log(`Request Received: ${req.body}. Inserted into database.`)
})

app.listen(port, () => {
    console.log(`Listening at http://localhost:${port}`)
})