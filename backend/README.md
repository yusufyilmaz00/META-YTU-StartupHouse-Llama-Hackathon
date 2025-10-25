# Backend

backend sistemi için readme dosyası

### Register Test
```
curl -X POST "http://localhost:8000/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "yusufspor00@gmail.com",
    "password": "123456789",
    "metadata": {"full_name": "Yusuf", "plan": "free"}
  }'
```

### Login Test

```
curl -s -X POST "http://localhost:8000/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "yusufspor00@gmail.com",
    "password": "123456789"
  }'
```

### Auth Me Test

```
curl "http://localhost:8000/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

