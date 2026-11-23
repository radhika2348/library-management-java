### Task1

- Branch feature/AddAuthor

# Add an author

A librarian needs one catalog record per writer so books can name that writer consistently.

## Requirement

The library keeps an author and can find that same author later.

## Information

- Name (required)
- Biography (optional)
- Year of birth (optional)
- Nationality (optional)

## Uniqueness Rules

- Two authors cannot share the same name.
- A second author with the same name is refused, and the first record is unchanged.
- unique index for authorName

## Who may do this

- Librarian and administrator: yes
- Member: no
- Anyone who is not signed in: no

### `POST /api/v1/authors`

Returns a bearer token.

- `201` with author payload
- `400` validation errors
- `409` duplicate author name

### Example requests

Register:

```bash
curl -s -X POST http://localhost:8080/api/v1/authors \
  -H 'Content-Type: application/json' \
  -d '{"name":"alexa","biography":"This Book tells the story of my life","yearOfBirth":2005,"nationality":"Italian"}'
```

## TASK 2

- Branch -feature/View-an-Author

## Who this is for

A signed-in person needs to open one author and read the record the library keeps.

## Requirement

Anyone who is signed in can open a single author that is already on file.

### `GET /api/v1/authors/{id}`

Returns author profile

- `200` author found
- `403` author was not found
- `404` unauthorized access

### Example requests

ViewAnAuthor:

View Author(replace `{id}` with an postman generated_authorId):

```bash
curl -s -X GET http://localhost:8080/api/v1/authors/{id} \
  -H 'Content-Type: application/json' \
```

## Task 3-Search Authors

# Who this is for

A signed-in person needs to find authors without already knowing which record to open.

## Requirement

Anyone who is signed in can page through authors and narrow the list by name.

### `GET /api/v1/authors`

Returns author profile

- `200` author found
- `404` unauthorized access

### Example requests

SearchUthors:

Use authorName to search:

```bash
curl -s -X GET http://localhost:8080/api/v1/authors?name='authorName'&page='value'&size='value' \
  -H 'Content-Type: application/json' \
```

## Task 4

## Update-An-Autnor

A librarian needs to correct an author's name or details after the record was first saved.

## Requirement

A librarian or an administrator can replace the saved details of one author. The updated author can still be found later.
