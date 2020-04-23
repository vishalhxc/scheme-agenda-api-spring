# Scheme Agenda API
APIs for defining a user's scheme details on an interval.

## Authentication
Authentication will be handled by a separate Auth API with its own data store. A token will be required for all requests on behalf of a User ID (stored within the token).

## Database Structure
### Groups
Groups a user's schemes
* ID: string
* Name: string
* UserId: string
* Color?: string

### Schemes
A goal to be accomplished.
* ID: string
* GroupID: GroupID (FK)
* UserId: string
* Name: string
* Interval: string (week, month, year, etc.)
* RoutinesPerInterval: integer
* IsCountDown: boolean

### Agendas
What needs to be done to satisfy one instance of a scheme 
* ID: string
* SchemeID: SchemeID (FK)
* Name: integer
* Sequence: integer
* Iterations: integer
* Repetitions: integer
* TargetNumber: integer
* Target: string