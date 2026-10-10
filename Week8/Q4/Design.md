# Employee Leave Request Management

## Classes

| Class | Attributes | Operations |
|---|---|---|
| Employee (abstract) | name | maxDaysPerRequest(), type() |
| FullTimeEmployee / PartTimeEmployee / ContractEmployee | | their own leave limits (15 / 7 / 3 days) |
| LeaveRequest | employee, from, to, status | days(), moveTo(next) |
| LeaveStatus (enum) | PENDING, APPROVED, REJECTED | canMoveTo(next) |
| LeaveManager | requests | submit(), review(), changeStatus() |

## Relationships and multiplicity

- Employee 1 ── 0..* LeaveRequest
- LeaveManager 1 ◆── 0..* LeaveRequest
- LeaveRequest * ── 1 LeaveStatus
- FullTimeEmployee, PartTimeEmployee, ContractEmployee ──▷ Employee

## State transitions

```
PENDING ──approve──▶ APPROVED
   │
   └────reject────▶ REJECTED
```

APPROVED and REJECTED are final states. `LeaveStatus.canMoveTo()` allows a move only out of PENDING, and `LeaveRequest.moveTo()` is the only way to change the status, so no caller can skip that rule.

## Flow

1. `submit()` creates a request in PENDING.
2. `review()` asks the employee for `maxDaysPerRequest()`. This is polymorphic, so the manager never checks the employee type. A request over the limit is rejected, otherwise it is approved.
3. Any later `changeStatus()` call goes through the same `moveTo()` check and is refused.

A new employee type (for example `Intern`) only needs a new `Employee` subclass. `LeaveManager` stays the same.
