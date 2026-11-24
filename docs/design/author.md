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

# Remove an author

## Who this is for

A librarian needs to take an author out of the catalog when that writer should no longer be offered for new books.

## Requirement

A librarian or an administrator can remove an author who has no books left in the catalog. After removal, that author cannot be found.

### `DELETE /api/v1/authors/{id}`

Returns author profile

- `200` author found and Deleted Successfully
- `404` unauthorized access

### Example requests

RemoveAnAuthor:

Remove Author(replace `{id}` with an postman generated_authorId):

```bash
curl -s -X DELETE http://localhost:8080/api/v1/authors/{id} \
  -H 'Content-Type: application/json' \
```

## Tie-Books-To-Authors

## Who this is for

Readers and librarians need a book to name a real author from the catalog, so a rename or a correction shows up everywhere that author is used.

## Requirement

A book names an author the catalog already knows. When that author's name changes, every book that names them shows the new name. Books already in the catalog remain readable.

### `PATCH /api/v1/books/{id}`

Returns bookID with updated AuthorName

- `200` Book updated with AuthorName in the catalog
- `400` Validation failed
- `401` Missing or invalid token
- `403` Insufficient role
- `404` Book not found
- `409` AuthorName not found

### Example requests

TieBooksToAuthors:

```bash
curl -s -X PATCH http://localhost:8080/api/v1/books/{id} \
  -H 'Content-Type: application/json' \
  -d '{"name":"alexa"}'
```
