
///<reference types="Cypress"/>
describe('Sessions spec', () => {
    beforeEach(() => {

        cy.login('yoga@studio.com','test!1234');
        cy.intercept(
            {
              method: 'GET',
              url: '/api/teacher',
            },
            [  {
                "id": 1,
                "lastName": "DELAHAYE",
                "firstName": "Margot",
                "createdAt": "2024-01-16T11:34:03",
                "updatedAt": "2024-01-16T11:34:03"
            },
            {
                "id": 2,
                "lastName": "THIERCELIN",
                "firstName": "Hélène",
                "createdAt": "2024-01-16T11:34:03",
                "updatedAt": "2024-01-16T11:34:03"
            }]).as('teacher')
      
      })
    
    it('add session', () => {
     
        cy.get('#create').click();
        cy.url().should('include','/create');
        cy.get('input[formControlName=name]').type("yoga");
        cy.get('input[formControlName=date]').type('2024-02-12');
        cy.get('mat-select[formControlName=teacher_id]')
            .click()
            .get('mat-option')
            .contains('DELAHAYE')
            .click();
        
        cy.get('textarea[formControlName=description]').type("yoga");
        cy.intercept('POST', '/api/session', {
            body: {
              name: 'yoga',
              date: '2024-02-12',
              description: 'yoga',
              teacher_id: 1,
              users:[1]
            },
          })
        cy.get('#save').click();
        cy.url().should('include','/session');
    })

})
