describe('Affichage des sessions', () => {
    beforeEach(() => {
      //simuler la liste des sessions
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
          { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
        ],
      }).as('getSessions');

      //simuler le login
      cy.visit('/login');
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
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
    });

   it('Devrait afficher la liste des sessions disponibles', () => {
      cy.get('.item').should('exist');
      cy.get('.item').should('have.length', 2);
      cy.get('.item').first().should('contain', 'Session 1');
      cy.get('.item').last().should('contain', 'Session 2');
    });

    it('Devrait afficher le bouton "Create" pour un admin', () => {
        cy.get('button[routerLink="create"]').should('be.visible')
      });
  });