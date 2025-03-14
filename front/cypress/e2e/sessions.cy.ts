describe('Sessions page', () => {
    beforeEach(() => {
      //simuler la liste des sessions
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
          { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
        ],
      }).as('getSessions');

      //login
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
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

      //simuler les teachers
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
       ]).as('teacher')

       //la requete get detail
        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: {
            id: 1, 
            name: 'Session 1', 
            date: '2025-03-01', 
            description: 'Yoga session', 
            teacher_id:1, users: [1], 
            createdAt: '2025-01-01', 
            updatedAt: null
          },
        });

        //la requete delete
        cy.intercept('DELETE', '/api/session/1', {
          statusCode: 200, // Réponse réussie
          body: { message: 'Session supprimée avec succès' }
        }).as('deleteSession');

        //simuler le teacher
        cy.intercept('GET', '/api/teacher/1', {
          statusCode: 200,
          body: {
            id: 1,
            lastName: "lastname1",
            firstName: "firstname1",
            createdAt: "2025-02-26T16:00:00",
            updatedAt: "2025-02-26T16:00:00"
          },
        }).as('getTeacher');
      
      });
    
      it('Devrait permettre à un admin d\'ajouter une session', () => {
        cy.get('button[routerLink="create"]').should('be.visible');
        cy.get('button[routerLink="create"]').click();
        cy.url().should('include', '/sessions/create');
    
        //Simulation de la requête POST pour ajouter une session
         cy.intercept('POST', '/api/session', {
          statusCode: 200,
          body: {
            id: 3,
            name: 'Nouvelle Session',
            date: '2025-04-01',
            description: 'Nouvelle session de test',
            users: [],
            teacher_id:1,
            createdAt: '2025-04-01',
            updatedAt: null,
          },
        }).as('createSession');
    
        // Remplir le formulaire
        cy.get('input[formControlName=name]').type('Nouvelle Session');
        cy.get('input[formControlName=date]').type('2025-04-01');
        cy.get('textarea[formControlName=description]').type('Nouvelle session de test');
        cy.get('mat-select[formControlName="teacher_id"]').click();
        cy.get('mat-option').contains('firstname1 lastname1').click();

        //simuler la nouvelle liste des sessions
         cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: [
            { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
            { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
            { id: 3, name: 'Nouvelle Session', date: '2025-04-01', description: 'Nouvelle session de test', teacher_id:1, users: [1], createdAt: '2025-04-01', updatedAt: null}
          ],
        }).as('getNewSessions');
    
        // Soumettre le formulaire
        cy.get('button[type="submit"]').click();

        cy.wait('@getNewSessions');
    
        // Vérifier que l'utilisateur est redirigé vers la liste des sessions
         cy.url().should('include', '/sessions');
    
        // Vérifier que la nouvelle session est bien affichée dans la liste
        cy.get('.item').should('have.length', 3);

        // Vérifier que le bouton "Detail" est bien visible pour la session 1 et voir son detail
        cy.get('.item').first().within(() => {
          cy.get('button').contains('Detail').should('be.visible').click();
        });

        cy.get('button').contains('Delete').should('be.visible');

        //simuler la nouvelle liste des sessions
        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: [
            { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
            { id: 3, name: 'Nouvelle Session', date: '2025-04-01', description: 'Nouvelle session de test', teacher_id:1, users: [1], createdAt: '2025-04-01', updatedAt: null}
          ],
        }).as('getNewSessions');

        cy.get('button').contains('Delete').click();
        
      }); 

})