describe('Logout spec', () => {
    beforeEach(() => {
        cy.visit('/login');

        //simuler la liste des sessions
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
          { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
        ],
      }).as('getSessions');

      });

    it('Logout successfull', () => {
        cy.intercept('POST', '/api/auth/login', {
          body: {
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
          },
        })
    
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
      
        cy.get('.link').contains('Logout').should('be.visible').click();
        cy.url().should('include', '/');

    });
})