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