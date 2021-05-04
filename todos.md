# Todos

## Task II

- Java Doc for interface functions

## Task III

- Implement tests

## Open questions

- Remove interfaces/exceptions from class diagram?
- Export-modules:
    - name `<module>.export` &rarr; problem due to dot and resulting package naming conventions?
    - (not export) modules have all dependencies of export modules + respective own exp. module. Is that even wanted?
    - In the future: Is `.export` to be the module to be added as dependency? If so: how do we get the impl (`<XyImpl>.java`)?
    - Is it okay/legal to get dependencies through dependencies? E.g. `game_administration` does only have one dependency (`game_administration.export`) but uses classes from other modules that are only imported in  `game_administration.export`
        - if so: How to manage versions if that is legal?
        - else: add dependencies to impl-modules (TODO)
    - assumption: Exclude all classes that are not exported? If not: undo commit `272871ac99ecfae68473c52179ff23e553ecc933`! (Note: Users' passwords are not exported because of security reasons!)
