describe('Sessions page', () => {
    beforeEach(() => {

        cy.login('yoga@studio.com','test!1234');
        cy.intercept(
            {
              method: 'GET',
              url: '/api/teacher',
            },
            [  {
                "id": 1,
                "lastName": "lastname1",
                "firstName": "firstname1",
                "createdAt": "2025-02-26T16:00:00",
                "updatedAt": "2025-02-26T16:00:00"
            },
            {
                "id": 2,
                "lastName": "lastname",
                "firstName": "firstname",
                "createdAt": "2025-02-26T16:00:00",
                "updatedAt": "2025-02-26T16:00:00"
            }]).as('teacher')
      
      })
    
    it('add session', () => {
     
        cy.get('#create').click();
        cy.url().should('include','/create');
        cy.get('input[formControlName=name]').type("yogaTest");
        cy.get('input[formControlName=date]').type('2025-01-22');
        cy.get('mat-select[formControlName=teacher_id]')
            .click()
            .get('mat-option')
            .contains('lastname1')
            .click();
        
        cy.get('textarea[formControlName=description]').type("yoga");
        cy.intercept('POST', '/api/session', {
            body: {
              name: 'yogaTest',
              date: '2025-01-22',
              description: 'yoga',
              teacher_id: 1,
              users:[1]
            },
          })
        cy.get('#save').click();
        cy.url().should('include','/session');
    })

})